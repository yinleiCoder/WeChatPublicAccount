package com.yinlei.wechat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/// 根据微信开发文档：菜单有很多类型。这里是 点击
@Getter
@Setter
@NoArgsConstructor
public class ClickButton extends AbstractButton{

    private String type = "click";
    private String key;

    public ClickButton(String name, String key) {
        super(name);
        this.key = key;
    }
}
