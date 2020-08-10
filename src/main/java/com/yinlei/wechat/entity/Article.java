package com.yinlei.wechat.entity;

import lombok.*;

// 图文消息
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
    private String title;
    private String description;
    private String picUrl;
    private String url;

}
