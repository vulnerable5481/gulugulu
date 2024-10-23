package com.zlc.gulu.server.interceptor;

import com.zlc.gulu.common.utils.GuluUtils;
import com.zlc.gulu.common.utils.UserHolder;
import com.zlc.gulu.pojo.vo.UserVo;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断用户是否有权限访问
        UserVo user = UserHolder.getUser();
        if (GuluUtils.isEmpty(user) || GuluUtils.isEmpty(user.getUserId())) {
            response.setStatus(401);//401 无权限
            return false;
        }
        //放行
        return true;
    }
}
