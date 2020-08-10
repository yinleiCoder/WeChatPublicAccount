package com.yinlei.wechat.service;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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

}
