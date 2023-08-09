package com.deng.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.FactoryBean;

public class DengFactoryBean implements FactoryBean {

    private SqlSession sqlSession;

    private Class mapperClass; // 通过这个Class属性，就能在动态的根据不同的mapper对象，去实现对象的FactoryBean并生成相对应的代理类

    public DengFactoryBean(Class mapperClass) {
        this.mapperClass = mapperClass;
    }


    public void setSqlSession(SqlSessionFactory sqlSessionFactory) {
        sqlSessionFactory.getConfiguration().addMapper(mapperClass);
        //问题，如果获得这些mapper的Class类呢？

        //初步解决
         /*
         * 使用beanDefinition(bean定义类)，并使用其中的方法告诉spring这个beanDefinition的类型-》DengFactoryBean以及构造方法mapperClass是什么
         * 利用registerBeanDefinition方法，讲该bean的定义注册进map中等待spring进行创建。
         * 调用spring的refresh()重新创建，发现实现了FactoryBean接口，所以还会继续生成getObject对应的bean
         * */

        //终极解决思路
        /*
        * 1.需要找一个注解，扫描出所有的mapper路径，并且获得所有mapper的Class
        * 2.例如初步解决的思路，将每一个mapper都有同样方法注册
        * */
        this.sqlSession = sqlSessionFactory.openSession();
    }

    @Override
    public Object getObject() throws Exception {

        return sqlSession.getMapper(mapperClass);

    }

    @Override
    public Class<?> getObjectType() {
        return mapperClass;
    }
}
