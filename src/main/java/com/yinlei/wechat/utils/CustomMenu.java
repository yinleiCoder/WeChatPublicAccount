package com.yinlei.wechat.utils;

import com.yinlei.wechat.entity.Button;
import com.yinlei.wechat.entity.ClickButton;
import com.yinlei.wechat.entity.SubButton;
import com.yinlei.wechat.entity.ViewButton;
import com.yinlei.wechat.service.WxService;
import net.sf.json.JSONObject;

/// 自定义菜单的事件处理
public class CustomMenu {
    public static void main(String[] args) {
        Button button = new Button();
        button.getButton().add(new ClickButton("一级点击", "1"));
        button.getButton().add(new ViewButton("一级跳转", "http://yinleilei.com"));
        SubButton subButton = new SubButton("子菜单");
        subButton.getSub_button().add(new ClickButton("去官网", "2"));
        subButton.getSub_button().add(new ClickButton("去百度", "3"));
        subButton.getSub_button().add(new ViewButton("去淘宝", "https://www.baidu.com"));
        button.getButton().add(subButton);
//        button.getButton().add(new AbstractButton("菜单一"));
//        button.getButton().add(new AbstractButton("菜单二"));
        JSONObject jsonObject = JSONObject.fromObject(button);
        System.out.println(jsonObject.toString());
        //{"button":[{"key":"1","name":"一级点击","type":"click"},{"name":"一级跳转","type":"view","url":"http://yinleilei.com"},{"name":"子菜单","sub_button":[{"key":"2","name":"去官网","type":"click"},{"key":"2","name":"去百度","type":"click"},{"name":"去淘宝","type":"view","url":"https://www.baidu.com"}]}]}
        String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
        String accessToken = WxService.getAccessTokenToExternal();
        System.out.println(accessToken);
        url = url.replace("ACCESS_TOKEN", accessToken);
        String menu = Utils.postCustomMenu(url, jsonObject.toString());
        System.out.println(menu);
//        报错：{"errcode":48001,"errmsg":"api unauthorized rid: 5f3234f1-5b08e1fa-19c6f259"}
//查看了官网全局返回码，发现我需要微信认证才能创建自定义菜单接口权限
//        所以这里没办法创建自定义菜单，只能手动创建了
    }
}
