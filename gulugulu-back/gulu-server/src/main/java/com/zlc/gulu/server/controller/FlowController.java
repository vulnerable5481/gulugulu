package com.zlc.gulu.server.controller;

import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.server.service.FlowService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/flow")
public class FlowController {

    @Resource
    private FlowService flowService;

    @PostMapping("/countPV")
    public Result countPV() {
        flowService.countPV();
        // 因为不是什么重要的数据，即使出现部分数据丢失也无所谓
        return Result.success();
    }

    @PostMapping("/countUV")
    public Result countUV(HttpServletRequest request) {
        flowService.countUV(request);
        return Result.success();
    }


}
