package com.yinlei.wechat.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.*;

import java.util.Map;

// 语音消息
@XStreamAlias("xml")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VoiceMessage extends BaseMessage{

    @XStreamAlias("MediaId")
    private String mediaId;

    public VoiceMessage(Map<String, String> requestMap, String mediaId) {
        super(requestMap);
        // 设置消息的msgType
        this.setMsgType("voice");
        this.mediaId = mediaId;
    }

}
