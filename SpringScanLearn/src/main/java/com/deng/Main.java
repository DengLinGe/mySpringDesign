package com.deng;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author: Deng.
 * @Description:
 * @Date Created in $YEAR -$MONTH -$DAY $TIME
 * @Modified By:
 */
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);

    }
}