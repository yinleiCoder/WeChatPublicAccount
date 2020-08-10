package com.yinlei.wechat.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.*;

import java.util.Map;

// 图片消息
@XStreamAlias("xml")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ImageMessage extends BaseMessage{

    @XStreamAlias("Content")
    private String mediaId;

    public ImageMessage(Map<String, String> requestMap, String mediaId) {
        super(requestMap);
        // 设置消息的msgType
        this.setMsgType("image");
        this.mediaId = mediaId;
    }
}
