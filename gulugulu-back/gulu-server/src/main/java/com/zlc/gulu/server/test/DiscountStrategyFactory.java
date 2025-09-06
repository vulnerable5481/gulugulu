package com.zlc.gulu.server.test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

public class DiscountStrategyFactory<T> implements CommandLineRunner {

    @Resource
    ApplicationContext applicationContext;
    private static final Map<String, DiscountStrategy> strategyMap = new HashMap<>();

    @Override
    public void run(String... args) throws Exception {
        Map<String, DiscountStrategy> map = applicationContext.getBeansOfType(DiscountStrategy.class);
        map.forEach((beanName, bean) -> {
            DiscountStrategy discountStrategy = strategyMap.get(bean.mark());
            strategyMap.put(bean.mark(),discountStrategy);
        });
    }

    public void chooseStrategy(String mark, T requestParam) {
        DiscountStrategy discountStrategy = strategyMap.get(mark);
        discountStrategy.discount(requestParam);
    }
}
