package com.deng.service;

import com.deng.service.aopInterface.UserInterface;
import com.deng.spring.DengApplicationContext;
import com.deng.spring.interfacePackage.BeanNameAware;

public class Test {
    public static void main(String[] args) {

        DengApplicationContext applicationContext = new DengApplicationContext(AppConfig.class);
        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.test();
    }
}
