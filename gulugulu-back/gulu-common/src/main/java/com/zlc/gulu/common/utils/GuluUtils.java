package com.zlc.gulu.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class GuluUtils {

    /*
    * 密码加盐 , 加密密码
    * */
    public static String hashPassword(String pwd){
        //生成盐
        byte[] b = new byte[16];
        Random random = new Random();
        random.nextBytes(b);

        //盐 转换为Base64字符串
        String salt = Base64.getEncoder().encodeToString(b);

        try {
            // 创建 SHA-256 消息摘要
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());//加入盐
            md.update(pwd.getBytes());//加入密码

            // 计算哈希值
            byte[] hashedBytes = md.digest();
            String hashedPassword = Base64.getEncoder().encodeToString(hashedBytes);

            //返回盐 和 加密后的密码  用分隔符拼接
            return salt + ":" + hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;//处理异常
        }
    }

    /*
    * 判断登录密码是否正确
    * */
    public static boolean verifyPassword(String input,String salt,String pwd){
        try {
            // 创建 SHA-256 消息摘要
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());//加入盐
            md.update(input.getBytes());//加入密码
            // 计算哈希值
            byte[] hashedBytes = md.digest();
            String hashedPassword = Base64.getEncoder().encodeToString(hashedBytes);

            //密码匹配 允许登录
            if(hashedPassword.equals(pwd)){
                return true;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;//有异常，登录失败
        }
        //除非检验合格，不然登录失败~
        return false;
    }

    /*
    * 判断非空
    * */
    public static boolean isEmpty(Object obj){
        if(obj == null){
            return true;
        }
        if(obj instanceof String){
            return ((String)obj).isEmpty();//空字符串
        }
        if(obj instanceof Collection){
            return ((Collection<?>) obj).isEmpty(); // 空集合
        }
        if(obj instanceof Map<?,?>){
            return ((Map<?,?>)obj).isEmpty();//空映射
        }
        if(obj.getClass().isArray()){
            return Array.getLength(obj) == 0;//空数组
        }
        return false;//其他情况不为空
    }

    /*
    * bean -> json
    * */
    public static String beanToJson(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = null;
        try {
            jsonStr = objectMapper.writeValueAsString(obj);
            return jsonStr;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";//发生异常返回空
        }
    }

    /*
     * json -> bean
     * */
    public static <T> T jsonToBean(String json, Class<T> clazz){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;//返回一个空对象
        }
    }
}
