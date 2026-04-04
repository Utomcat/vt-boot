package com.ranyk.vt.boot.mq.util;

import com.ranyk.vt.boot.base.constant.MessageKeyEnum;
import com.ranyk.vt.boot.mq.domain.dto.BaseMessageDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * CLASS_NAME: MessageUtils.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 消息工具处理类
 * @date: 2026-03-26
 */
@Slf4j
public class MessageUtils {

    /**
     * 从消息队列传输的消息对象中根据指定 KEY 获取对应的数据
     *
     * @param messageDTO 消息传输对象
     * @param key        消息 KEY 枚举对象
     * @return 消息数据中的指定 KEY 的值
     */
    public static String getValueFromDataByKey(BaseMessageDTO messageDTO, MessageKeyEnum key) {
        Map<String, Object> body = messageDTO.getBody();
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) body.get(MessageKeyEnum.MESSAGE_BODY_DATA_KEY.getValue());
        return switch (key) {
            case MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_NAME_KEY ->
                    (String) data.get(MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_NAME_KEY.getValue());
            case MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_TYPE_KEY ->
                    (String) data.get(MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_TYPE_KEY.getValue());
            case MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_PARAM_KEY ->
                    (String) data.get(MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_PARAM_KEY.getValue());
            case MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_OPERATOR_KEY ->
                    (String) data.get(MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_OPERATOR_KEY.getValue());
            case MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_MODULE_KEY ->
                    (String) data.get(MessageKeyEnum.MESSAGE_BODY_DATA_LOG_OPERATION_RECORD_MODULE_KEY.getValue());
            default -> {
                log.error("获取消息数据失败, 未知的消息枚举 KEY {}, 请检查是否存在对应的 KEY!", key);
                yield "";
            }
        };

    }
}
