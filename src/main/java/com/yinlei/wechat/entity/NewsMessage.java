package com.yinlei.wechat.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 图文消息
@XStreamAlias("xml")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewsMessage extends BaseMessage{

    private String articleCount;
    private List<Article> articles = new ArrayList<>();


    public NewsMessage(Map<String, String> requestMap, String articleCount, List<Article> articles) {
        super(requestMap);
        // 设置消息的msgType
        this.setMsgType("news");
        this.articleCount = articleCount;
        this.articles = articles;

    }

}
