package com.my.spring.service;

import com.my.spring.annotation.MyComponent;
import com.my.spring.factory.FactoryBean;

@MyComponent("person")
public class MyFactoryBean implements FactoryBean {
    public Object getObject() {
        return new Person();
    }
}
