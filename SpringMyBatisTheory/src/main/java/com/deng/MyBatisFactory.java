package com.deng;

import org.springframework.beans.factory.FactoryBean;

/**
 * @Author: Deng.
 * @Description:
 * @Date Created in 2023 -08 -06 22:00
 * @Modified By:
 */
public class MyBatisFactory implements FactoryBean {

//    FactoryBean的特殊之处在于它可以向容器中注册两个Bean，一个是它本身，一个是FactoryBean.getObject()方法返回值所代表的Bean


    //作用：返回对象实例，通过这一步，可以返回bean的代理对象，例如可以自定义jdk的动态代理进行返回
    @Override
    public Object getObject() throws Exception {
        System.out.println("实现了这个东西");
        return null;
    }

    //返回bean的类型
    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
