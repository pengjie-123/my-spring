package com.my.spring.tomcat;

import com.my.spring.annotation.MyAutowired;
import com.my.spring.annotation.MyComponent;
import com.my.spring.processor.ServletBeanPostProcessor;

@MyComponent
public class UrlMappingHandler {
    @MyAutowired ServletBeanPostProcessor servletBeanPostProcessor;

    public Object invoke(String[] mappings) {
        try {
            String method = mappings[0];
            String url = "";
            if (mappings.length > 1) {
                url = mappings[1];
            } else {
                //TODO THIS IS A STATIC REQUEST
                url = "/";
            }

            ServletWrapper servlet = servletBeanPostProcessor.getServlets().get(url);
            if (servlet == null) {
                return ResponseUtil.get404();
            }
            Object result = servlet.getMethod().invoke(servlet.getTarget());
            return ResponseUtil.get200(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.get500(e);
        }
    }
}
