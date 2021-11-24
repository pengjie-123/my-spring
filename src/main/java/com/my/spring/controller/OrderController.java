package com.my.spring.controller;

import com.my.spring.annotation.MyAutowired;
import com.my.spring.annotation.MyComponent;
import com.my.spring.annotation.MyController;
import com.my.spring.annotation.MyRequestMapping;
import com.my.spring.service.OrderService;

@MyComponent
@MyController
public class OrderController {
    @MyAutowired OrderService orderService;

    @MyRequestMapping("/order/save")
    public String createNew() {
        orderService.save("123aaa");
        return "create success";
    }

    @MyRequestMapping("/order/find")
    public String find() {
        orderService.find();
        return "find success";
    }
}
