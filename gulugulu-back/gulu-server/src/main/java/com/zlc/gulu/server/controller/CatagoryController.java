package com.zlc.gulu.server.controller;

import com.zlc.gulu.common.result.Result;
import com.zlc.gulu.pojo.entity.CatagoryEntity;
import com.zlc.gulu.server.service.CatagoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(("/catagory"))
public class CatagoryController {

    @Resource
    private CatagoryService catagoryService;

    /*
     *  获取分类树
     * */
    @GetMapping("/list/tree")
    public Result listWithTree() {
        List<CatagoryEntity> list = catagoryService.listWithTree();
        return Result.success(list);
    }

    /*
     *  新增分类
     * */
    @PostMapping("/save")
    public Result save(@RequestBody CatagoryEntity catagory) {
        catagoryService.save(catagory);
        return Result.success();
    }
}
