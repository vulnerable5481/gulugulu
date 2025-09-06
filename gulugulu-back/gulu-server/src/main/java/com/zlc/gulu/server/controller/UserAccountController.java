package com.zlc.gulu.server.controller;

import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.pojo.vo.UserLoginVo;
import com.zlc.gulu.pojo.vo.UserRegisterVo;
import com.zlc.gulu.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserAccountController {

    @Resource
    private UserService userService;

    /*
     * 用户登录接口
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserLoginVo userLoginVo) {
        return userService.login(userLoginVo);
    }

    /*
     * 用户注册接口
     * */
    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterVo userRegisterVo) {
        return userService.register(userRegisterVo);
    }

    /*
     *  退出登录
     * */
    @PostMapping("/exit")
    public Result exit(@RequestParam("token") String token) {
        Result exit = null;
        try {
            exit = userService.exit(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exit;
    }
}
