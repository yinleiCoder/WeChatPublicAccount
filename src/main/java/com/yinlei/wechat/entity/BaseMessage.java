package com.yinlei.wechat.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.*;

import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseMessage {

    @XStreamAlias("ToUserName")
    private String toUserName;

    @XStreamAlias("FromUserName")
    private String fromUserName;

    @XStreamAlias("CreateTime")
    private String createTime;

    @XStreamAlias("MsgType")
    private String msgType;

    public BaseMessage(Map<String, String> requestMap) {
        this.toUserName = requestMap.get("FromUserName");
        this.fromUserName = requestMap.get("ToUserName");
        this.createTime = System.currentTimeMillis()/1000+"";
    }
}
