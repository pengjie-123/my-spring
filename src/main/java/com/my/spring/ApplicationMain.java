package com.my.spring;

import com.my.spring.factory.MyApplicationContext;
import com.my.spring.service.OrderService;
import com.my.spring.service.Person;
import com.my.spring.service.UserService;
import com.my.spring.tomcat.Tomcat;

public class ApplicationMain {
    public static void main(String[] args) throws Exception {
        run();
    }

    private static void run() throws Exception {
        MyApplicationContext context     = new MyApplicationContext(ApplicationConfig.class);
        Tomcat tomcat = (Tomcat) context.getBean("tomcat");
        tomcat.startServer();
    }

    private static void testIocAndAop() throws Exception {
        MyApplicationContext context     = new MyApplicationContext(ApplicationConfig.class);
        UserService userService = (UserService) context.getBean("userService");
        Person person = (Person) context.getBean("person");
        Object p =  context.getBean("&person");
        Object test =  context.getBean("testService");
        System.out.println("---------------------------------------------------");
        System.out.println(userService);
        System.out.println(person);
        System.out.println(p);
        System.out.println(test);
        userService.test();
        System.out.println("------------------------------------------------------");
        // this is a proxy bean
        OrderService orderService = (OrderService) context.getBean("orderService");
        orderService.save("aaaskkknn123");
        System.out.println("------------------------------------------------------");
        orderService.find();
    }
}
