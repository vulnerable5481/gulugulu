package com.zlc.gulu.server.test;

public interface DiscountStrategy<T> {

    void discount(T requestParam);

    String mark();
}
