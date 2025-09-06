package com.zlc.gulu.server.service;


import javax.servlet.http.HttpServletRequest;

/**
 * @author 赵联城
 * @description 针对流量推送的Service
 * @createDate 2025-2-23
 */
public interface FlowService {

    void countPV();

    void countUV(HttpServletRequest request);
}
