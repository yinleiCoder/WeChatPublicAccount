package com.yinlei.wechat.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.*;

import java.util.Map;

// 视频消息
@XStreamAlias("xml")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VideoMessage extends BaseMessage{
    @XStreamAlias("MediaId")
    private String mediaId;

    @XStreamAlias("Title")
    private String title;

    @XStreamAlias("Description")
    private String description;

    public VideoMessage(Map<String, String> requestMap, String mediaId, String title, String description) {
        super(requestMap);
        // 设置消息的msgType
        this.setMsgType("video");
        this.mediaId = mediaId;
        this.title = title;
        this.description = description;
    }
}
