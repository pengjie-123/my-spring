package com.my.spring.aop;

import java.lang.reflect.Method;

public class InterceptorAdvisor {
    private Method method;

    private String expression;

    private Class<?> targetClazz;

    private String targetMethod;

    private Object self;

    private Boolean isBefore;

    private Boolean isAfter;

    private Boolean isAround;

    public Boolean before() {
        return isBefore;
    }

    public void setBefore(Boolean before) {
        isBefore = before;
    }

    public Boolean after() {
        return isAfter;
    }

    public void setAfter(Boolean after) {
        isAfter = after;
    }

    public Boolean around() {
        return isAround;
    }

    public void setAround(Boolean around) {
        isAround = around;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Class<?> getTargetClazz() throws ClassNotFoundException {
        return Class.forName(this.expression.substring(0, this.expression.lastIndexOf(".")));
    }

    public String getTargetMethod() {
        return this.expression.substring(this.expression.lastIndexOf(".") + 1, this.expression.length());
    }


    public Object getSelf() {
        return self;
    }

    public void setSelf(Object self) {
        this.self = self;
    }
}
