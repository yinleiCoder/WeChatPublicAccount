package com.yinlei.wechat.controller;

import com.yinlei.wechat.service.WxService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx")
public class WxController {


    /**
     * 微信开发者接入
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param echostr 随机字符串
     * @return echostr
     */
    @GetMapping("/")
    public String helloWx(String signature, String timestamp, String nonce, String echostr) {
        // 根据微信要求，需要返回echostr判断是否来自微信服务器,否则将会出现验证失败
//        http://ylwechat.free.idcfengye.com/wx/
//        yinleitoken
//        Tzj7hyu4XJeutp2dM5jEptlG9Pxms0tkLLUHcVU9IL7

        if (WxService.check(timestamp, nonce, signature)) {
            return echostr;
        }else {
            System.out.println("微信服务器接入失败!");
        }
        return "";
    }

    /**
     * 接收用户发来的消息
     */
    @PostMapping("/")
    public void receiveCommonUserMessage() {
        System.out.println("公众号用户发来的消息");

    }

}
