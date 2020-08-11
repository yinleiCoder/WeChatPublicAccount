package com.yinlei.wechat.service;

import com.thoughtworks.xstream.XStream;
import com.yinlei.wechat.entity.*;
import com.yinlei.wechat.utils.Utils;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.yinlei.wechat.utils.Utils.net;

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
     * 处理文本消息： https://www.juhe.cn/ 聚合数据
     * 星座运势:https://www.juhe.cn/docs/api/id/58
     * 笑话大全:https://www.juhe.cn/docs/api/id/95
     * 天气预报:https://www.juhe.cn/docs/api/id/73
     * 新闻头条:https://www.juhe.cn/docs/api/id/235
     *IP地址查询：https://www.juhe.cn/docs/api/id/1
     * @param requestMap
     * @return
     */
    private static BaseMessage handleTextMessage(Map<String, String> requestMap) {
        // 用户发来的内容
        String msg = requestMap.get("Content");
        if (msg.equals("图文")) {
            List<Article> articles = new ArrayList<>();
            articles.add(new Article("图文消息标题", "图文消息的详细介绍", "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=307923566,1492523027&fm=26&gp=0.jpg", "http://yinleilei.com"));
            NewsMessage nm = new NewsMessage(requestMap, articles);
            return nm;
        }
        // 调用天气预报API
        String answer = weatherForecast(msg);

        TextMessage textMessage = new TextMessage(requestMap, answer);
        return textMessage;
    }

    private static String weatherForecast(String msg) {
            //根据城市查询天气
            String result =null;
            String url ="http://op.juhe.cn/onebox/weather/query";//请求接口地址
            Map params = new HashMap();//请求参数
            params.put("cityname", msg);//要查询的城市，如：温州、上海、北京
            params.put("key",Utils.APPKEY);//应用APPKEY(应用详细页查询)
            params.put("dtype","");//返回数据的格式,xml或json，默认json

            try {
                result =Utils.net(url, params, "GET");
                System.out.println(result);
                JSONObject object = JSONObject.fromObject(result);
                if(object.getInt("error_code")==0){
//                    {"reason":"查询成功!","result":{"data":{"realtime":{"city_code":"101270401","city_name":"绵阳","date":"2020-08-11","time":"10:00:00","week":"2","moon":"六月廿二","dataUptime":1597112208,"weather":{"temperature":"27","humidity":"81","info":"小雨","img":"07"},"wind":{"direct":"东北风","power":"1级","offset":"","windspeed":""}},"life":{"date":"2020-08-11","info":{"kongtiao":["部分时间开启","天气热，同时湿度很大，您将会感到有些闷热，因此建议在午后较热的时候开启制冷空调。"],"guomin":["极不易发","天气条件极不易诱发过敏，有较强降水，空气湿润，出行注意携带雨具。"],"shushidu":["较不舒适","白天虽然有雨，但仍无法削弱较高气温带来的暑意，同时降雨造成湿度加大会您感到有些闷热，不很舒适。"],"chuanyi":["热","天气热，建议着短裙、短裤、短薄外套、T恤等夏季服装。"],"diaoyu":["不宜","天气不好，不适合垂钓。"],"ganmao":["少发","各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。"],"ziwaixian":["弱","紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。"],"xiche":["不宜","不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"],"yundong":["较不宜","有较强降水，建议您选择在室内进行健身休闲运动。"],"daisan":["带伞","有较强降水，您在外出的时候一定要带雨伞，以免被雨水淋湿。"]}},"weather":[{"date":"2020-08-11","info":{"dawn":["3","阵雨","24","持续无风向","微风","19:50"],"day":["9","大雨","29","持续无风向","微风","06:23","出门记得带伞，行走驾驶做好防滑准备"],"night":["10","暴雨","23","持续无风向","微风","19:49","出门记得带伞，行走驾驶做好防滑准备"]},"week":"二","nongli":"六月廿二"},{"date":"2020-08-12","info":{"dawn":["10","暴雨","23","持续无风向","微风","19:49"],"day":["9","大雨","27","持续无风向","微风","06:24","出门记得带伞，行走驾驶做好防滑准备"],"night":["9","大雨","21","持续无风向","微风","19:48","出门记得带伞，行走驾驶做好防滑准备"]},"week":"三","nongli":"六月廿三"},{"date":"2020-08-13","info":{"dawn":["9","大雨","21","持续无风向","微风","19:48"],"day":["9","大雨","29","持续无风向","微风","06:24"],"night":["9","大雨","23","持续无风向","微风","19:47"]},"week":"四","nongli":"六月廿四"},{"date":"2020-08-14","info":{"dawn":["9","大雨","23","持续无风向","微风","19:47"],"day":["7","小雨","29","持续无风向","微风","06:25"],"night":["8","中雨","23","持续无风向","微风","19:46"]},"week":"五","nongli":"六月廿五"},{"date":"2020-08-15","info":{"dawn":["8","中雨","23","持续无风向","微风","19:46"],"day":["8","中雨","27","持续无风向","微风","06:26"],"night":["8","中雨","23","持续无风向","微风","19:45"]},"week":"六","nongli":"六月廿六"}],"f3h":{"temperature":[{"jg":"20200811080000","jb":"27"},{"jg":"20200811110000","jb":"28"},{"jg":"20200811140000","jb":"28"},{"jg":"20200811170000","jb":"27"},{"jg":"20200811200000","jb":"25"},{"jg":"20200811230000","jb":"25"},{"jg":"20200812020000","jb":"24"},{"jg":"20200812050000","jb":"23"},{"jg":"20200812080000","jb":"24"}],"precipitation":[{"jg":"20200811080000","jf":"0.5"},{"jg":"20200811110000","jf":3},{"jg":"20200811140000","jf":3},{"jg":"20200811170000","jf":15},{"jg":"20200811200000","jf":15},{"jg":"20200811230000","jf":15},{"jg":"20200812020000","jf":15},{"jg":"20200812050000","jf":25},{"jg":"20200812080000","jf":15}]},"pm25":{"pm25":{"level":1,"quality":"优","des":"空气很棒，快出门呼吸新鲜空气吧。","curPm":"46","pm25":"30","pm10":"46","pub_time":1597107600,"city_code":"101270401"},"cityName":"绵阳","key":"绵阳","dateTime":"2020年08月11日09时"},"jingqu":"","jingqutq":"","date":"","isForeign":"0","partner":{"title_word":"全国","show_url":"tianqi.so.com","base_url":"http:\/\/tianqi.so.com\/weather\/101270401"}}},"error_code":0}
//                    System.out.println(object.get("result"));
                    JSONObject result1 = object.getJSONObject("result");
                    JSONObject data = result1.getJSONObject("data");
                    JSONObject realtime = data.getJSONObject("realtime");
                    String city_name = realtime.getString("city_name");
                    String date = realtime.getString("date");
                    String time = realtime.getString("time");
                    String week = "星期"+realtime.getString("week");
                    JSONObject weather = realtime.getJSONObject("weather");
                    String temperature = "温度："+weather.getString("temperature");
                    String humidity = "湿度："+weather.getString("humidity");
                    String info = "天气情况："+weather.getString("info");

                    return city_name+"\n"+temperature+"\n"+humidity+"\n"+info+"\n"+date+"\n"+time+"\n"+week;
                }else{
                    System.out.println(object.get("error_code")+":"+object.get("reason"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;

    }

}
