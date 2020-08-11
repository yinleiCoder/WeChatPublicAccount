package com.yinlei.wechat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// 面向对象封装公众号的自定义菜单
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Button {

    private List<AbstractButton> button = new ArrayList<>();


}
