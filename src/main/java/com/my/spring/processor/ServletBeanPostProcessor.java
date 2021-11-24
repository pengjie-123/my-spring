package com.my.spring.processor;

import com.my.spring.tomcat.ServletWrapper;
import com.my.spring.annotation.MyComponent;
import com.my.spring.annotation.MyController;
import com.my.spring.annotation.MyRequestMapping;
import com.my.spring.factory.BeanPostProcessor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@MyComponent
public class ServletBeanPostProcessor implements BeanPostProcessor {
    private Map<String, ServletWrapper> servlets = new HashMap<>();

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        if (bean.getClass().isAnnotationPresent(MyController.class)) {
            for (Method method : bean.getClass().getDeclaredMethods()) {
                MyRequestMapping mapping = method.getDeclaredAnnotation(MyRequestMapping.class);
                if (mapping == null) {
                    continue;
                }
                servlets.put(mapping.value(), new ServletWrapper(method, bean));
            }
        }
        return bean;
    }

    public Map<String, ServletWrapper> getServlets() {
        return servlets;
    }
}
