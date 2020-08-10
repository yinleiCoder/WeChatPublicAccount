package com.yinlei.wechat.service;

import com.thoughtworks.xstream.XStream;
import com.yinlei.wechat.entity.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WxService {

    private static final String WX_TOKEN = "yinleitoken";

    /**
     * 微信开发者验证签名
     * 1）将token、timestamp、nonce三个参数进行字典序排序
     * 2）将三个参数字符串拼接成一个字符串进行sha1加密
     * 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
     * @param timestamp
     * @param nonce
     * @param signature
     * @return
     */
    public static boolean check(String timestamp,String nonce, String signature) {
        String[] strs = new String[] {WX_TOKEN, timestamp, nonce};
        Arrays.sort(strs);
        String str = strs[0] + strs[1] + strs[2];
        String mysig = sha1(str);
        return mysig.equals(signature);
    }

    /**
     * 字符串进行sha1加密
     * @param src
     * @return
     */
    public static String sha1(String src) {
        try {
            // 获取加密对象
            MessageDigest md = MessageDigest.getInstance("sha1");
            // 加密
            byte[] digest = md.digest(src.getBytes());
            // 处理加密结果
            char[] chars = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
            StringBuilder sb= new StringBuilder();
            for(byte b: digest) {
                sb.append(chars[(b>>4)&15]);// 高4位右移4位 15：1111
                sb.append(chars[b&15]);// 处理低4位
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析XML：dom4j
     * 通用的解析：因为文本消息、图片消息官方给的示例都是xml数据，长得差不多，这里都封装成一个map就行了
     * @param is 输入流
     * @return xml解析好的map（包含用户发送的消息）
     */
    public static Map<String, String> handleUserSendTextMessage(InputStream is) {
        Map<String, String> map= new HashMap<>();
        SAXReader reader = new SAXReader();
        try {
            // 读取输入流，获取文档对象
            Document document = reader.read(is);
            // 根据文档对象获取根节点
            Element root = document.getRootElement();
            // 获取根节点所有的子节点
            List<Element> elements = root.elements();
            for (Element e : elements) {
                map.put(e.getName(), e.getStringValue());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 被动回复用户消息:
     * 1 回复文本消息
     *
     * 2 回复图片消息
     *
     * 3 回复语音消息
     *
     * 4 回复视频消息
     *
     * 5 回复音乐消息
     *
     * 6 回复图文消息
     * @param requestMap
     * @return 返回通用的xml数据包
     */
    public static String handleReplyToUserMsg(Map<String, String> requestMap) {
        BaseMessage msg = null;
        String msgType = requestMap.get("MsgType");
        switch (msgType) {
            case "text":// 文本消息
                msg = handleTextMessage(requestMap);
                break;
            case "image":
                break;
            case "voice":
                break;
            case "video":
                break;
            case "music":
                break;
            case "news":
                break;
            default:
                break;
        }
//        System.out.println(msg);
        // 将消息对象转换为xml
        if (msg != null) {
            return handleBeanToXML(msg);
        }
        return null;
    }

    /**
     * 将消息对象转换为xml
     * @param msg
     * @return
     */
    private static String handleBeanToXML(BaseMessage msg) {
        // 将java对象转化为xml数据
        // 需要处理的@XStreamAlias("xml")注解的类
        XStream stream = new XStream();
        stream.processAnnotations(TextMessage.class);
        stream.processAnnotations(ImageMessage.class);
        stream.processAnnotations(MusicMessage.class);
        stream.processAnnotations(NewsMessage.class);
        stream.processAnnotations(VideoMessage.class);
        stream.processAnnotations(VoiceMessage.class);
        String xml = stream.toXML(msg);
        return xml;
    }

    /**
     * 处理文本消息
     * @param requestMap
     * @return
     */
    private static BaseMessage handleTextMessage(Map<String, String> requestMap) {
        TextMessage textMessage = new TextMessage(requestMap, "文本消息测试成功！");
        return textMessage;
    }
}
