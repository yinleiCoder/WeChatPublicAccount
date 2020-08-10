package com.yinlei.wechat.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.*;

import java.util.Map;

/// 文本消息
@XStreamAlias("xml")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TextMessage extends BaseMessage{

    @XStreamAlias("Content")
    private String content;

    public TextMessage(Map<String, String> requestMap, String content) {
        super(requestMap);
        // 设置消息的msgType
        this.setMsgType("text");
        this.content = content;
    }

}
