package com.yinlei.wechat.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/// 二级菜单
@Getter
@Setter
@NoArgsConstructor
public class SubButton extends AbstractButton{

    private List<AbstractButton> sub_button = new ArrayList<>();


    public SubButton(String name) {
        super(name);
    }
}
