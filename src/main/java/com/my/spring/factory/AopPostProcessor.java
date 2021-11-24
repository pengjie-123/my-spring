package com.my.spring.factory;

public abstract class AopPostProcessor implements BeanPostProcessor {
    private MyApplicationContext beanFactory;

    public MyApplicationContext getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(MyApplicationContext beanFactory) {
        this.beanFactory = beanFactory;
    }
}
