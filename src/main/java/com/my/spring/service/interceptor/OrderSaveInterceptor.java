package com.my.spring.service.interceptor;

import com.my.spring.aop.MyJoinPoint;
import com.my.spring.annotation.MyAspect;
import com.my.spring.annotation.MyComponent;
import com.my.spring.annotation.advice.MyAfter;
import com.my.spring.annotation.advice.MyBefore;

@MyAspect
@MyComponent
public class OrderSaveInterceptor {


    @MyBefore("com.my.spring.service.impl.OrderServiceImpl.save")
    public void before(MyJoinPoint joinPoint) {
        System.out.println("OrderSaveInterceptor: before save order, do something: method -> " + joinPoint.getSignature() + ", args -> " + joinPoint.getArgs()[0]);
    }

    @MyAfter("com.my.spring.service.impl.OrderServiceImpl.save")
    public void after(MyJoinPoint joinPoint) {
        System.out.println("OrderSaveInterceptor: after save order, do something: method -> " + joinPoint.getSignature() + ", args -> " + joinPoint.getArgs()[0]);
    }
}
