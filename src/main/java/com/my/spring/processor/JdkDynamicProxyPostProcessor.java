package com.my.spring.processor;

import com.my.spring.aop.InterceptorAdvisor;
import com.my.spring.aop.JdkDynamicProxy;
import com.my.spring.annotation.MyComponent;
import com.my.spring.factory.AopPostProcessor;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;

@MyComponent
public class JdkDynamicProxyPostProcessor extends AopPostProcessor {
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return wrapIfNecessary(bean, beanName, this.getBeanFactory().getAdvisors());
    }

    private static Object wrapIfNecessary(Object bean, String beanName, Collection<InterceptorAdvisor> advisors) throws ClassNotFoundException {
        Collection<InterceptorAdvisor> matchedAdvisors = new ArrayList<>();
        for (InterceptorAdvisor advisor : advisors) {
            if (advisor.getTargetClazz() == bean.getClass()) {
                matchedAdvisors.add(advisor);
            }
        }
        if (matchedAdvisors.isEmpty()) {
            return bean;
        }
        return Proxy.newProxyInstance(bean.getClass().getClassLoader(),
                                      bean.getClass().getInterfaces(),
                                      new JdkDynamicProxy(matchedAdvisors, bean));
    }
}
