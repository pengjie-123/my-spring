package com.my.spring.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

public class JdkDynamicProxy implements InvocationHandler {
    private Collection<InterceptorAdvisor> advisors;
    private Object target;

    public JdkDynamicProxy(Collection<InterceptorAdvisor> advisors, Object target) {
        this.advisors = advisors;
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object retValue;
        Collection<InterceptorAdvisor> beforeAdvisors = new ArrayList<>();
        Collection<InterceptorAdvisor> afterAdvisors = new ArrayList<>();
        for (InterceptorAdvisor ad : advisors) {
            if (ad.getTargetMethod().equals(method.getName())) {
                if (ad.before() != null && ad.before()) beforeAdvisors.add(ad);
                if (ad.after() != null && ad.after()) afterAdvisors.add(ad);
            }
        }
        if (beforeAdvisors.size() > 0) {
           for (InterceptorAdvisor advisor : beforeAdvisors) {
               advisor.getMethod().invoke(advisor.getSelf(), getJoinPointArgs(method, args));
           }
        }

        retValue = method.invoke(target, args);

        if (afterAdvisors.size() > 0) {
            for (InterceptorAdvisor advisor : afterAdvisors) {
                advisor.getMethod().invoke(advisor.getSelf(), getJoinPointArgs(method, args));
            }
        }

        return retValue;
    }

    private static MyJoinPoint getJoinPointArgs(Method method, Object[] args) {
        MyJoinPoint jp = new MyJoinPoint();
        jp.setArgs(args);
        jp.setSignature(method.getName());
        return jp;
    }
}
