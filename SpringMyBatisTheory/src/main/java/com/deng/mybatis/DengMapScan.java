package com.deng.mybatis;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(DengImportBeanDefinitionRegistrar.class) //也可以放在appConfig中
public @interface DengMapScan {

    String value() default ""; //mapper扫描路径
}
