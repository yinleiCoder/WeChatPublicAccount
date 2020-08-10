package com.yinlei.wechat.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.*;

import java.util.Map;

// 音乐消息
@XStreamAlias("xml")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MusicMessage extends BaseMessage{


    private Music music;




    public MusicMessage(Map<String, String> requestMap, Music music) {
        super(requestMap);
        // 设置消息的msgType
        this.setMsgType("music");
        this.music = music;
    }

}
