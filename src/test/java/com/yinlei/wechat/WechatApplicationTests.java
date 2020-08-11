package com.yinlei.wechat;

import com.thoughtworks.xstream.XStream;
import com.yinlei.wechat.entity.*;
import com.yinlei.wechat.service.WxService;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static com.yinlei.wechat.service.WxService.getAccessTokenToExternal;

@SpringBootTest
class WechatApplicationTests {

    @Test
    void contextLoads() {
//        Map<String, String> map = new HashMap<>();
//        map.put("ToUserName", "to");
//        map.put("FromUserName", "from");
//        map.put("MsgType", "type");
//        TextMessage tm = new TextMessage(map,"你好啊！！！！！");
//
//        // 将java对象转化为xml数据
//        // 需要处理的@XStreamAlias("xml")注解的类
//        XStream stream = new XStream();
//        stream.processAnnotations(TextMessage.class);
//        stream.processAnnotations(ImageMessage.class);
//        stream.processAnnotations(MusicMessage.class);
//        stream.processAnnotations(NewsMessage.class);
//        stream.processAnnotations(VideoMessage.class);
//        stream.processAnnotations(VoiceMessage.class);
//        String xml = stream.toXML(tm);
//        System.out.println(xml);
//

//        System.out.println(getAccessTokenToExternal());;
//        System.out.println(getAccessTokenToExternal());;
//        WxService.getAccessToken();

        Button button = new Button();
        button.getButton().add(new ClickButton("一级点击", "1"));
        button.getButton().add(new ViewButton("一级跳转", "http://yinleilei.com"));
        SubButton subButton = new SubButton("子菜单");
        subButton.getSub_button().add(new ClickButton("去官网", "2"));
        subButton.getSub_button().add(new ClickButton("去百度", "2"));
        subButton.getSub_button().add(new ViewButton("去淘宝", "https://www.baidu.com"));
        button.getButton().add(subButton);
//        button.getButton().add(new AbstractButton("菜单一"));
//        button.getButton().add(new AbstractButton("菜单二"));
        JSONObject jsonObject = JSONObject.fromObject(button);
        System.out.println(jsonObject.toString());
        //{"button":[{"key":"1","name":"一级点击","type":"click"},{"name":"一级跳转","type":"view","url":"http://yinleilei.com"},{"name":"子菜单","sub_button":[{"key":"2","name":"去官网","type":"click"},{"key":"2","name":"去百度","type":"click"},{"name":"去淘宝","type":"view","url":"https://www.baidu.com"}]}]}

    }

}
