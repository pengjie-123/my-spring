package com.my.spring.service.impl;

import com.my.spring.annotation.MyAutowired;
import com.my.spring.annotation.MyComponent;
import com.my.spring.factory.InitializingBean;
import com.my.spring.service.OrderService;
import com.my.spring.service.UserService;

@MyComponent("userService")
public class UserServiceImpl implements UserService, InitializingBean {

    @MyAutowired OrderService orderService;
    public void test() {
        System.out.println("this is a test");
        orderService.save("rest-ref");
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println(this.getClass().getName() + "is executing after property settings, property: " + orderService.getClass().getName());
    }
}
