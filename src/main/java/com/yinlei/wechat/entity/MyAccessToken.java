package com.yinlei.wechat.entity;

import lombok.*;

// 我的AccessToken：注意会过期
// 这里为了方便，我直接计算出什么时候过期
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MyAccessToken {

    private String accessToken;
    private long expireTime; // 过期的时间

    public MyAccessToken(String accessToken, String expireIn) {
        this.accessToken = accessToken;
        expireTime = System.currentTimeMillis() + Integer.parseInt(expireIn) * 1000; // 计算出过期时间
    }

    // AccessToken是否过期了
    public boolean isExpired() {
        return System.currentTimeMillis() > expireTime;
    }

}
