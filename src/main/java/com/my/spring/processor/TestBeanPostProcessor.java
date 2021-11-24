package com.my.spring.processor;

import com.my.spring.annotation.MyComponent;
import com.my.spring.factory.BeanPostProcessor;

@MyComponent
public class TestBeanPostProcessor implements BeanPostProcessor {
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        System.out.println("postProcess Before Initialization -> " + bean.getClass().getName());
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        System.out.println("postProcess After Initialization -> " + bean.getClass().getName());
        return bean;
    }
}
