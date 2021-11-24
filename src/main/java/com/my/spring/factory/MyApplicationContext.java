package com.my.spring.factory;

import com.my.spring.aop.InterceptorAdvisor;
import com.my.spring.annotation.MyAspect;
import com.my.spring.annotation.MyAutowired;
import com.my.spring.annotation.MyComponent;
import com.my.spring.annotation.MyComponentScan;
import com.my.spring.annotation.advice.MyAfter;
import com.my.spring.annotation.advice.MyAround;
import com.my.spring.annotation.advice.MyBefore;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyApplicationContext {

    private Class<?> config;

    private final Map<String, Object> singletons = new HashMap<>();
    private final Map<String, Class>  clazzMap = new HashMap<>();
    private final List<BeanPostProcessor> postProcessors = new ArrayList<>();
    private List<InterceptorAdvisor> advisors = new ArrayList<>();
    private List<String> clazzNames = new ArrayList<>();

    public List<InterceptorAdvisor> getAdvisors() {
        return advisors;
    }

    public MyApplicationContext(Class<?> config) throws Exception {
        this.config = config;
        if (config.isAnnotationPresent(MyComponentScan.class)) {
            scan();
            prepareBeanPostProcessors();
            prepareAdvisors();
            finishSingletons();
        }
    }

    private void finishSingletons() throws Exception {
        for (String beanName : clazzMap.keySet()) {
            Class<?> clazz = clazzMap.get(beanName);
            if (this.singletons.get(beanName) == null) {
                createBean(clazz, beanName);
            }
        }
    }

    private void scan() throws ClassNotFoundException {
        MyComponentScan scan        = this.config.getAnnotation(MyComponentScan.class);
        String          path        = scan.value();
        ClassLoader     classLoader = MyApplicationContext.class.getClassLoader();
        URL             resource    = classLoader.getResource(path.replaceAll("\\.", "/"));
        assert resource != null;
        File              file       = new File(resource.getFile());
        fetchAllClazzNames(file, path);
        for (String clazzName : clazzNames) {
            Class<?>    clazz     = classLoader.loadClass(clazzName.replaceAll("/", "."));
            MyComponent component = clazz.getAnnotation(MyComponent.class);
            if (component != null) {
                String beanName = component.value();
                if (beanName.equals("")) {
                    beanName = getDefaultBeanName(clazz.getSimpleName());
                }
                clazzMap.put(beanName, clazz);
            }
        }
    }

    private static String getDefaultBeanName(String beanName) {
        if (beanName.length() == 1) {
            return beanName.toLowerCase();
        }
        return beanName.substring(0, 1).toLowerCase() + beanName.substring(1, beanName.length());
    }

    private Object createBean(
        Class<?> clazz,
        String beanName
    ) throws Exception {
        if (clazz == null) {
            return null;
        }
        Object bean = doCreateBean(clazz);

        if (bean instanceof FactoryBean) {
            singletons.put("&" + beanName, bean);
            bean = ((FactoryBean) bean).getObject();
        }

        bean = applyPostProcessorBeforeInit(bean, beanName);

        init(bean);

       bean = applyPostProcessorAfterInit(bean, beanName);

        singletons.put(beanName, bean);
        return bean;
    }

    private Object applyPostProcessorBeforeInit(Object bean, String beanName) throws Exception {
        if (bean instanceof BeanPostProcessor) {
            return bean;
        }
        for (BeanPostProcessor processor : postProcessors) {
            bean = processor.postProcessBeforeInitialization(bean, beanName);
        }
        return bean;
    }

    private Object applyPostProcessorAfterInit(Object bean, String beanName) throws Exception {
        if (bean instanceof BeanPostProcessor) {
            return bean;
        }
        for (BeanPostProcessor processor : postProcessors) {
            bean = processor.postProcessAfterInitialization(bean, beanName);
        }
        return bean;
    }

    private Object doCreateBean(Class<?> clazz) throws Exception {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        Object         bean        = constructor.newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            MyAutowired annotation = field.getAnnotation(MyAutowired.class);
            if (annotation == null) {
                continue;
            }
            String fieldName = field.getName();
            Object obj       = getBean(fieldName);
            if (obj == null) {
                throw new IllegalArgumentException("can not find this bean: " + fieldName);
            }
            field.set(bean, obj);
        }
        return bean;
    }

    private void init(Object bean) throws Exception {
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }
    }

    public Object getBean(String beanName) throws Exception {
        Object o = this.singletons.get(beanName);
        if (o == null) {
            return createBean(clazzMap.get(beanName), beanName);
        }
        return o;
    }

    private void fetchAllClazzNames(File file, String path) {
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                fetchAllClazzNames(f, path);
            } else {
                String name = f.getPath();
                if (f.getName().endsWith(".class")) {
                    name = name.substring(name.indexOf(path.replaceAll("\\.", "/")), name.indexOf(".class"));
                    clazzNames.add(name);
                }
            }
        }
    }

    private void prepareBeanPostProcessors() throws Exception {
        for (String beanName : clazzMap.keySet()) {
            Class<?> clazz = clazzMap.get(beanName);
            if (BeanPostProcessor.class.isAssignableFrom(clazz)) {
                Object processor = getBean(beanName);
                if (processor instanceof AopPostProcessor) {
                    ((AopPostProcessor) processor).setBeanFactory(this);
                }
                postProcessors.add((BeanPostProcessor) processor);
            }
        }
    }

    private void prepareAdvisors() throws Exception {
        for (String beanName : clazzMap.keySet()) {
            Class<?> clazz = clazzMap.get(beanName);
            if (clazz.isAnnotationPresent(MyAspect.class)) {
                // this is a advisor bean
                Object self = getBean(beanName);
                for (Method m : clazz.getDeclaredMethods()) {
                    if (m.isAnnotationPresent(MyBefore.class)) {
                        MyBefore before = m.getAnnotation(MyBefore.class);
                        InterceptorAdvisor advisor = new InterceptorAdvisor();
                        advisor.setExpression(before.value());
                        advisor.setMethod(m);
                        advisor.setSelf(self);
                        advisor.setBefore(true);
                        this.advisors.add(advisor);
                    }
                    else if (m.isAnnotationPresent(MyAfter.class)) {
                        MyAfter after  = m.getAnnotation(MyAfter.class);
                        InterceptorAdvisor advisor = new InterceptorAdvisor();
                        advisor.setExpression(after.value());
                        advisor.setMethod(m);
                        advisor.setSelf(self);
                        advisor.setAfter(true);
                        this.advisors.add(advisor);
                    }
                    else if (m.isAnnotationPresent(MyAround.class)) {
                        //TODO
                    }
                }
            }
        }
    }
}
