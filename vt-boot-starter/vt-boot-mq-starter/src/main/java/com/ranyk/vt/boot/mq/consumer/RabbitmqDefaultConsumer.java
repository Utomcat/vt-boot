package com.ranyk.vt.boot.mq.consumer;

import com.rabbitmq.client.Channel;
import com.ranyk.vt.boot.base.constant.MessageKeyEnum;
import com.ranyk.vt.boot.mq.domain.dto.BaseMessageDTO;
import com.ranyk.vt.boot.mq.util.MessageUtils;
import com.ranyk.vt.boot.rpc.service.IOperationRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

/**
 * CLASS_NAME: RabbitmqDefaultConsumer.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Rabbitmq 默认队列消息监听消费者
 * @date: 2026-03-25
 */
@Slf4j
@Service
@ConditionalOnBean(IOperationRecordService.class)
public class RabbitmqDefaultConsumer {

    /**
     * 日志服务接口对象
     */
    private final IOperationRecordService operationRecordService;

    /**
     * 构造函数 - 向 Spring IOC 容器中自动注入日志服务接口对象
     *
     * @param operationRecordService 日志服务接口对象, {@link IOperationRecordService}
     */
    @Autowired
    @SuppressWarnings("all")
    public RabbitmqDefaultConsumer(IOperationRecordService operationRecordService) {
        this.operationRecordService = operationRecordService;
    }

    /**
     * 接收 Rabbitmq 默认队列 消息
     * <p>
     * 通过使用注解 @RabbitListener 配置 Rabbitmq 的监听信息. <br/>
     * 通过注解 @RabbitListener 的属性 bindings 配置对应的绑定信息,<br/>
     * 其中通过注解 @QueueBinding 配置 队列的绑定信息,<br/>
     * 从而无需进行手动配置队列、交换机、绑定关系的 Bean,<br/>
     * 该注解可以自动检测对应的 队列、交换机 的 Bean 和 队列、交换机、路由key 的绑定关系,<br/>
     * 不存在则自动创建对应的 Bean 并 绑定相关的 路由 key,存在则不进行创建, 但会使用对应的 Bean<br/>
     * <ul>
     *     <li>
     *     属性 bindings, 其中注解 @QueueBinding 注解的属性说明如下:
     *     <ul>
     *       <li>key: 交换机 和 队列 绑定的 路由 KEY 名称</li>
     *       <li>exchange: 对应的交换机 Bean 配置
     *       <ul>
     *           <li>value: 交换机名称, 该值没有配置时, 读取 name 属性的值</li>
     *           <li>name: 交换机名称, 该值没有配置时, 读取 value 属性的值</li>
     *           <li>type: 交换机类型, 默认为 {@link ExchangeTypes#DIRECT} 具体类型参见 {@link ExchangeTypes}</li>
     *           <li>durable: 是否持久化, 默认为 true, 即持久化</li>
     *           <li>autoDelete: 是否自动删除, 默认为 false, 即不删除</li>
     *           <li>其他参数参见 {@link Exchange}</li>
     *        </ul>
     *       </li>
     *       <li>value: 对应的队列 Bean 配置
     *       <ul>
     *           <li>value: 队列名称, 该值没有配置时, 读取 name 属性的值</li>
     *           <li>name: 队列名称, 该值没有配置时, 读取 value 属性的值</li>
     *           <li>durable: 是否持久化, 默认为 ""</li>
     *           <li>exclusive: 是否排他, 默认为 ""</li>
     *           <li>autoDelete: 是否自动删除, 默认为 ""</li>
     *           <li>其他参数参见 {@link Queue}</li>
     *       </ul>
     *       </li>
     *     </ul>
     *     </li>
     *     <li>
     *      属性 messageConverter, 用于指定当前接受消息时需要的消息转换器对象的 Bean 名称, 默认为 SimpleMessageConverter
     *     </li>
     * </ul>
     * </P>
     *
     * @param messageDTO 接收到的消息传输对象
     * @param channel 消息通道对象 {@link  Channel}
     * @param message 消息对象 {@link Message}
     */
    @RabbitListener(
            // 配置 RabbitMQ 队列绑定关系
            bindings = @QueueBinding(
                    // 配置 RabbitMQ 队列的 路由 KEY 名称
                    key = "default_routing_key",
                    // 配置 RabbitMQ 队列的 交换机名称
                    exchange = @Exchange(value = "default_exchange"),
                    // 配置 RabbitMQ 队列名称, 并设置为持久化队列
                    value = @Queue(value = "default_queue", durable = "true")
            ))
    public void receiveMessage(BaseMessageDTO messageDTO, Channel channel, Message message) throws Exception {
        try {
            Boolean saveResult = operationRecordService.saveOneOperationRecord(
                    MessageUtils.getValueFromDataByKey(messageDTO, MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_NAME_KEY),
                    MessageUtils.getValueFromDataByKey(messageDTO, MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_TYPE_KEY),
                    MessageUtils.getValueFromDataByKey(messageDTO, MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_PARAM_KEY),
                    MessageUtils.getValueFromDataByKey(messageDTO, MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_OPERATOR_KEY),
                    MessageUtils.getValueFromDataByKey(messageDTO, MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_MODULE_KEY));
            if (!saveResult) {
                log.error("同步保存操作日志记录失败, 使用文本记录模式记录日志!");
            }

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("接收消息发生异常, 错误信息为: ", e);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
}
