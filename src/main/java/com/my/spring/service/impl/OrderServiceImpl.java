package com.my.spring.service.impl;

import com.my.spring.annotation.MyAutowired;
import com.my.spring.annotation.MyComponent;
import com.my.spring.service.OrderService;

@MyComponent("orderService")
public class OrderServiceImpl implements OrderService {
    @MyAutowired TestServiceImpl testService;
    public void save(String ref) {
        System.out.println("an order " + ref + " was created successfully");
    }

    @Override public void find() {
        System.out.println("find some order");
    }
}
