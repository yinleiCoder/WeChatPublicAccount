package com.yinlei.wechat;

import com.thoughtworks.xstream.XStream;
import com.yinlei.wechat.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class WechatApplicationTests {

    @Test
    void contextLoads() {
        Map<String, String> map = new HashMap<>();
        map.put("ToUserName", "to");
        map.put("FromUserName", "from");
        map.put("MsgType", "type");
        TextMessage tm = new TextMessage(map,"你好啊！！！！！");

        // 将java对象转化为xml数据
        // 需要处理的@XStreamAlias("xml")注解的类
        XStream stream = new XStream();
        stream.processAnnotations(TextMessage.class);
        stream.processAnnotations(ImageMessage.class);
        stream.processAnnotations(MusicMessage.class);
        stream.processAnnotations(NewsMessage.class);
        stream.processAnnotations(VideoMessage.class);
        stream.processAnnotations(VoiceMessage.class);
        String xml = stream.toXML(tm);
        System.out.println(xml);
    }

}
