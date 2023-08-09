package com.deng.service;

import com.deng.spring.Component;
import com.deng.spring.interfacePackage.BeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: Deng.
 * @Description:
 * @Date Created in 2023 -08 -03 13:42
 * @Modified By:
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(String beanName, Object bean) {
        if (beanName.equals("userService")){
            System.out.println("这是userService：postProcessBeforeInitialization.....");
        }



        return bean;
    }


    //代理对象的执行顺序
    // UserService.class --> 构造方法 -->普通对象 --> 依赖注入 --> 初始化前、后 --> aop生成代理对象 --> 将普通对象（和代理对象）放入池子
    //执行代理对象的方法时，本质上是持有一个普通对象，先执行代理对象的切面再执行普通的原来逻辑

    @Override
    public Object postProcessAfterInitialization(String beanName, Object bean) {
        if (beanName.equals("userService")){
            System.out.println("这是userService：postProcessAfterInitialization.....");
            // 测试使用jdk的动态代理创建代理对象
            Object proxyInstance = Proxy.newProxyInstance(MyBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("这里是切面逻辑........");
                    return method.invoke(bean, args);
                }
            });

            return proxyInstance;
        }
        return bean;
    }
}
