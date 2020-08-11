package com.yinlei.wechat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/// 根据微信开发文档：菜单有很多类型。这里是 view
@Getter
@Setter
@NoArgsConstructor
public class ViewButton extends  AbstractButton{

    private String type = "view";
    private String url;

    public ViewButton(String name, String url) {
        super(name);
        this.type = type;
        this.url = url;
    }

}
