package com.yinlei.wechat.controller;

import com.yinlei.wechat.service.WxService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

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
     * 接收用户发来的消息和事件推送：
     * 当普通微信用户向公众账号发消息时，微信服务器将POST消息的XML数据包到开发者填写的URL上。
     * 关于重试的消息排重，推荐使用msgid排重。
     * 微信服务器在五秒内收不到响应会断掉连接，并且重新发起请求，总共重试三次。假如服务器无法保证在五秒内处理并回复，可以直接回复空串，微信服务器不会对此作任何处理，并且不会发起重试。详情请见“发送消息-被动回复消息”。
     * 如果开发者需要对用户消息在5秒内立即做出回应，即使用“发送消息-被动回复消息”接口向用户被动回复消息时，可以在
     * 公众平台官网的开发者中心处设置消息加密。开启加密后，用户发来的消息和开发者回复的消息都会被加密（但开发者通过客服接口等API调用形式向用户发送消息，则不受影响）。
     *
     * 文本消息：
     * ToUserName	开发者微信号
     * FromUserName	发送方帐号（一个OpenID）
     * CreateTime	消息创建时间 （整型）单位：秒
     * MsgType	消息类型，文本为text
     * Content	文本消息内容
     * MsgId	消息id，64位整型
     */
    @PostMapping("/")
    public void receiveCommonUserMessage() {
//        System.out.println("公众号用户发来的消息");
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        // 处理用户发来的消息和推送
        try {
            Map<String, String> requestMap = WxService.handleUserSendTextMessage(request.getInputStream());
//            System.out.println(requestMap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
