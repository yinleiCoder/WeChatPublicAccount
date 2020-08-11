package com.yinlei.wechat.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.*;

// 图文消息
@XStreamAlias("item")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Article {
//      <Title><![CDATA[title1]]></Title>
//      <Description><![CDATA[description1]]></Description>
//      <PicUrl><![CDATA[picurl]]></PicUrl>
//      <Url><![CDATA[url]]></Url>

    @XStreamAlias("Title")
    private String title;

    @XStreamAlias("Description")
    private String description;

    @XStreamAlias("PicUrl")
    private String picUrl;

    @XStreamAlias("Url")
    private String url;

}
