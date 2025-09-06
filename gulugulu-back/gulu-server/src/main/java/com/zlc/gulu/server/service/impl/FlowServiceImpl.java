package com.zlc.gulu.server.service.impl;

import com.zlc.gulu.common.constant.RedisConstant;
import com.zlc.gulu.common.utils.GuluUtils;
import com.zlc.gulu.server.service.FlowService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 赵联城
 * @description 针对流量推送的Service
 * @createDate 2025-2-23
 */

@Service
public class FlowServiceImpl implements FlowService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 统计Page View,实现PV自增
     */
    @Override
    public void countPV() {
        String pvKey = RedisConstant.PV_KEY;
        String pvValue = stringRedisTemplate.opsForValue().get(pvKey);
        if (pvValue == null || pvValue == "") {
            // 说明第一次PV自增，初始化
            stringRedisTemplate.opsForValue().set(pvKey, String.valueOf(1));
        } else {
            int pv = Integer.parseInt(pvValue);
            // PV自增
            stringRedisTemplate.opsForValue().set(pvKey, String.valueOf(++pv));
        }

    }

    /**
     * 根据IP,统计Unique Visitor
     * 【目前尚未可知该方法getIPAddr是否能够找到真正的公网IP？？？？】
     */
    @Override
    public void countUV(HttpServletRequest request) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String uvSetKey = RedisConstant.UV_SET + now.format(formatter);
        // 获取用户IP地址
        String ip = getIpAddr(request);
        // 如果IP地址获取错误，或者获取的是代理服务器的IP地址【权当看不见，当成一个用户得了】
        if (ip == null || ip == "") {
            return;
        }
        // 添加到uvSet集合中【实际上采用的是HyperLogLog算法】
        stringRedisTemplate.opsForHyperLogLog().add(uvSetKey, ip);
    }


    /*
     *   获取用户ID地址 【但是真实有效性 还有待考察 !!!!!】
     * */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP，多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }
}















