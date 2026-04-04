package com.ranyk.vt.boot.mq.factory;

import com.lmax.disruptor.EventFactory;
import com.ranyk.vt.boot.mq.domain.dto.BaseMessageDTO;

/**
 * CLASS_NAME: DisruptorDefaultMessageEventFactory.java
 *
 * @author ranyk
 * @version V1.0
 * @description: disruptor 消息队列默认工厂
 * @date: 2026-03-24
 */
public class DisruptorDefaultMessageEventFactory implements EventFactory<BaseMessageDTO> {

    /**
     * Implementations should instantiate an event object, with all memory already allocated where possible.
     *
     * @return T newly constructed event instance.
     */
    @Override
    public BaseMessageDTO newInstance() {
        return new BaseMessageDTO();
    }
}
