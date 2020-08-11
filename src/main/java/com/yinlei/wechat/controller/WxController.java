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
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
     *
     * 被动回复用户消息：
     * 当用户发送消息给公众号时（或某些特定的用户操作引发的事件推送时），会产生一个POST请求，开发者可以在响应包（Get）中返回特定XML结构，来对该消息进行响应（现支持回复文本、图片、图文、语音、视频、音乐）。严格来说，发送被动响应消息其实并不是一种接口，而是对微信服务器发过来消息的一次回复。
     * 微信服务器在将用户的消息发给公众号的开发者服务器地址（开发者中心处配置）后，微信服务器在五秒内收不到响应会断掉连接，并且重新发起请求，总共重试三次，如果在调试中，发现用户无法收到响应的消息，可以检查是否消息处理超时。关于重试的消息排重，有msgid的消息推荐使用msgid排重。事件类型消息推荐使用FromUserName + CreateTime 排重。
     * 如果开发者希望增强安全性，可以在开发者中心处开启消息加密，这样，用户发给公众号的消息以及公众号被动回复用户消息都会继续加密，详见被动回复消息加解密说明。
     * 假如服务器无法保证在五秒内处理并回复，必须做出下述回复，这样微信服务器才不会对此作任何处理，并且不会发起重试（这种情况下，可以使用客服消息接口进行异步回复），否则，将出现严重的错误提示。详见下面说明：
     * 1、直接回复success（推荐方式） 2、直接回复空串（指字节长度为0的空字符串，而不是XML结构体中content字段的内容为空）
     *
     */
    @PostMapping("/")
    public void receiveCommonUserMessage() throws UnsupportedEncodingException {
//        System.out.println("公众号用户发来的消息");
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();
        request.setCharacterEncoding("utf8");
        response.setCharacterEncoding("utf8");

        // 处理用户发来的消息和推送
        try {
            Map<String, String> requestMap = WxService.handleUserSendTextMessage(request.getInputStream());
//            System.out.println(requestMap);
            // 回复用户消息
//            String respnseXMLData = "<xml>\n" +
//                    "  <ToUserName><![CDATA["+requestMap.get("FromUserName")+"]]></ToUserName>\n" +
//                    "  <FromUserName><![CDATA["+requestMap.get("ToUserName")+"]]></FromUserName>\n" +
//                    "  <CreateTime>"+System.currentTimeMillis()/1000+"</CreateTime>\n" +
//                    "  <MsgType><![CDATA[text]]></MsgType>\n" +
//                    "  <Content><![CDATA["+"你好!!!"+"]]></Content>\n" +
//                    "</xml>\n";
            String respnseXMLData = WxService.handleReplyToUserMsg(requestMap);
            PrintWriter writer = response.getWriter();
            writer.print(respnseXMLData);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
