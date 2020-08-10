package com.yinlei.wechat.entity;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Music {
    private String title;
    private String description;
    private String musicURL;
    private String hQMusicUrl;
    private String thumbMediaId;
}
