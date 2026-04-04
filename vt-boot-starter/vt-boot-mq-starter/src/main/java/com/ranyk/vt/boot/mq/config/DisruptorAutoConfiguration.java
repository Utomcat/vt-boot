package com.ranyk.vt.boot.mq.config;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.ranyk.vt.boot.mq.domain.dto.BaseMessageDTO;
import com.ranyk.vt.boot.mq.factory.DisruptorDefaultMessageEventFactory;
import com.ranyk.vt.boot.mq.factory.DisruptorMqMessageHandlerFactory;
import com.ranyk.vt.boot.mq.handler.DisruptorDefaultMqMessageHandler;
import com.ranyk.vt.boot.mq.publisher.DisruptorDefaultMqMessagePublisher;
import com.ranyk.vt.boot.mq.strategy.impl.EmailDisruptorMqMessageHandlerStrategyImpl;
import com.ranyk.vt.boot.mq.strategy.impl.LogDisruptorMqMessageHandlerStrategyImpl;
import com.ranyk.vt.boot.mq.strategy.impl.SmsDisruptorMqMessageHandlerStrategyImpl;
import com.ranyk.vt.boot.rpc.service.IOperationRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.concurrent.ThreadFactory;

/**
 * CLASS_NAME: DisruptorAutoConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Disruptor 队列自动配置类
 * @date: 2026-03-24
 */
@Slf4j
@AutoConfiguration
@ConditionalOnProperty(prefix = "vt.boot.mq", name = "is-enable-disruptor", havingValue = "true")
public class DisruptorAutoConfiguration {
    // Ring Buffer 大小，必须是 2 的幂
    private static final int BUFFER_SIZE = 1024;

    /**
     * 创建 DisruptorMqMessageHandlerFactory Bean 对象
     *
     * @param logMqMessageHandlerStrategyImpl   {@link LogDisruptorMqMessageHandlerStrategyImpl} Bean 对象
     * @param emailMqMessageHandlerStrategyImpl {@link EmailDisruptorMqMessageHandlerStrategyImpl} Bean 对象
     * @param smsMqMessageHandlerStrategyImpl   {@link SmsDisruptorMqMessageHandlerStrategyImpl} Bean 对象
     * @return 创建的 {@link DisruptorMqMessageHandlerFactory} Bean 对象
     */
    @Bean
    @ConditionalOnMissingBean
    public DisruptorMqMessageHandlerFactory disruptorMqMessageHandlerFactory(LogDisruptorMqMessageHandlerStrategyImpl logMqMessageHandlerStrategyImpl,
                                                                             EmailDisruptorMqMessageHandlerStrategyImpl emailMqMessageHandlerStrategyImpl,
                                                                             SmsDisruptorMqMessageHandlerStrategyImpl smsMqMessageHandlerStrategyImpl) {
        return new DisruptorMqMessageHandlerFactory(Arrays.asList(logMqMessageHandlerStrategyImpl, emailMqMessageHandlerStrategyImpl, smsMqMessageHandlerStrategyImpl));
    }

    /**
     * 创建 EmailDisruptorMqMessageHandlerStrategyImpl Bean 对象
     *
     * @return 创建的 {@link EmailDisruptorMqMessageHandlerStrategyImpl} Bean 对象
     */
    @Bean
    @ConditionalOnMissingBean
    public EmailDisruptorMqMessageHandlerStrategyImpl emailMqMessageHandlerStrategyImpl() {
        return new EmailDisruptorMqMessageHandlerStrategyImpl();
    }

    /**
     * 创建 LogDisruptorMqMessageHandlerStrategyImpl Bean 对象
     *
     * @return 创建的 {@link LogDisruptorMqMessageHandlerStrategyImpl} Bean 对象
     */
    @Bean
    @ConditionalOnMissingBean
    public LogDisruptorMqMessageHandlerStrategyImpl logMqMessageHandlerStrategyImpl(IOperationRecordService operationRecordService) {
        return new LogDisruptorMqMessageHandlerStrategyImpl(operationRecordService);
    }

    /**
     * 创建 SmsDisruptorMqMessageHandlerStrategyImpl Bean 对象
     *
     * @return 创建的 {@link SmsDisruptorMqMessageHandlerStrategyImpl} Bean 对象
     */
    @Bean
    @ConditionalOnMissingBean
    public SmsDisruptorMqMessageHandlerStrategyImpl smsMqMessageHandlerStrategyImpl() {
        return new SmsDisruptorMqMessageHandlerStrategyImpl();
    }

    /**
     * 创建 DisruptorDefaultMqMessageHandler Bean 对象
     *
     * @param disruptorMqMessageHandlerFactory {@link DisruptorMqMessageHandlerFactory} Bean 对象
     * @return 创建的 {@link DisruptorDefaultMqMessageHandler} Bean 对象
     */
    @Bean
    @ConditionalOnMissingBean
    public DisruptorDefaultMqMessageHandler disruptorDefaultMqMessageHandler(DisruptorMqMessageHandlerFactory disruptorMqMessageHandlerFactory) {
        return new DisruptorDefaultMqMessageHandler(disruptorMqMessageHandlerFactory);
    }

    /**
     * 创建 Disruptor Bean 对象
     *
     * @param disruptorDefaultMqMessageHandler {@link DisruptorDefaultMqMessageHandler} Bean 对象
     * @return 返回 {@link Disruptor}&lt;BaseMessageDTO&gt; 对象
     */
    @ConditionalOnMissingBean
    @Bean(destroyMethod = "shutdown")
    public Disruptor<BaseMessageDTO> disruptor(DisruptorDefaultMqMessageHandler disruptorDefaultMqMessageHandler) {
        // 事件工厂
        DisruptorDefaultMessageEventFactory factory = new DisruptorDefaultMessageEventFactory();
        // 线程工厂（用于 Disruptor 内部线程）
        ThreadFactory threadFactory = Thread.ofVirtual().name("disruptor-vt-", 0).factory();
        // ProducerType：SINGLE（单生产者）或 MULTI（多生产者）
        ProducerType producerType = ProducerType.SINGLE;
        // 等待策略：可选择 BlockingWaitStrategy、YieldingWaitStrategy 等
        BlockingWaitStrategy waitStrategy = new BlockingWaitStrategy();
        // 构建 Disruptor
        Disruptor<BaseMessageDTO> disruptor = new Disruptor<>(factory, BUFFER_SIZE, threadFactory, producerType, waitStrategy);
        // 设置事件处理器（可多个）
        disruptor.handleEventsWith(disruptorDefaultMqMessageHandler);
        // 启动 Disruptor
        disruptor.start();
        return disruptor;
    }

    /**
     * 创建 RingBuffer Bean 对象
     *
     * @param disruptor Disruptor Bean 对象
     * @return 创建的 {@link RingBuffer}&lt;BaseMessageDTO&gt; 对象
     */
    @Bean
    @ConditionalOnMissingBean
    public RingBuffer<BaseMessageDTO> ringBuffer(Disruptor<BaseMessageDTO> disruptor) {
        return disruptor.getRingBuffer();
    }

    /**
     * 创建 DisruptorDefaultMqMessagePublisher Bean 对象
     *
     * @param ringBuffer RingBuffer Bean 对象
     * @return 创建的 {@link DisruptorDefaultMqMessagePublisher} Bean 对象
     */
    @Bean
    @ConditionalOnMissingBean
    public DisruptorDefaultMqMessagePublisher disruptorDefaultMqMessagePublisher(RingBuffer<BaseMessageDTO> ringBuffer) {
        return new DisruptorDefaultMqMessagePublisher(ringBuffer);
    }
}
