package com.my.spring.factory;

public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
