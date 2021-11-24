package com.my.spring.tomcat;

import java.lang.reflect.Method;

public class ServletWrapper {
    private String url;
    private Method method;
    private Object target;

    public ServletWrapper(
        Method method,
        Object target
    ) {
        this.method = method;
        this.target = target;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
