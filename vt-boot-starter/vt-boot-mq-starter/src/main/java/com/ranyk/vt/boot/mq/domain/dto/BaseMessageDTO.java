package com.ranyk.vt.boot.mq.domain.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * CLASS_NAME: BaseMessageDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 异步消息数据传输基础 DTO 类,属性说明<br/>
 * @date: 2026-03-24
 */
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@ToString(callSuper=true)
public class BaseMessageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2775946040914879052L;
    /**
     * 消息头 - 用来存放一些固定公共的属性, 如消息通知标题、类型等公共属性
     */
    @Builder.Default
    private Map<String, Object> header = new HashMap<>(16);
    /**
     * 消息体 - 用来存放业务相关的属性, 如用户信息、商品信息等业务属性
     */
    @Builder.Default
    private Map<String, Object> body = new HashMap<>(16);

 }
