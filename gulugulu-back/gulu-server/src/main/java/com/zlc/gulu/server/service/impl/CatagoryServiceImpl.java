package com.zlc.gulu.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.gulu.pojo.entity.CatagoryEntity;
import com.zlc.gulu.server.mapper.CatagoryMapper;
import com.zlc.gulu.server.service.CatagoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 赵联城
 * @description 针对表【gl_catagory(分类表)】的数据库操作Service实现
 * @createDate 2024-11-10 22:07:31
 */
@Service
public class CatagoryServiceImpl extends ServiceImpl<CatagoryMapper, CatagoryEntity>
        implements CatagoryService {

    @Override
    public List<CatagoryEntity> listWithTree() {
        // 查出所有分类
        List<CatagoryEntity> list = this.list();

        //组装成父子的树形结构
        return listTree(list);
    }

    /*
     *  组装并返回父子结构的树形分类
     * */
    private List<CatagoryEntity> listTree(List<CatagoryEntity> all) {
        // 抽出一级分类
        List<CatagoryEntity> res = all.stream()
                .filter(item -> item.getParentId() == 0) // pId=0 则为一级分类
                .map(item -> {
                    item.setChildren(getChildren(item, all)); // 设置子分类
                    return item;
                }).collect(Collectors.toList());


        return res;
    }

    /*
     *  获取父分类的孩子
     * */
    private List<CatagoryEntity> getChildren(CatagoryEntity parent, List<CatagoryEntity> all) {
        Integer pId = parent.getCatryId();
        List<CatagoryEntity> children = all.stream()
                .filter(item -> item.getParentId() == pId)
                .collect(Collectors.toList());

        return children;
    }

}




