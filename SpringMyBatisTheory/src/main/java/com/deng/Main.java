package com.deng;

import com.deng.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author: Deng.
 * @Description:
 * @Date Created in $YEAR -$MONTH -$DAY $TIME
 * @Modified By:
 */
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = (UserService)context.getBean("userService");
        System.out.println(userService);
    }
}