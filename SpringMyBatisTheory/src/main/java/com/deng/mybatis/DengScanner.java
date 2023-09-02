package com.deng.mybatis;


import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Set;

public class DengScanner extends ClassPathBeanDefinitionScanner {

    public DengScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }


    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) { //符合什么条件才会过滤
        return beanDefinition.getMetadata().isInterface();
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        //basePackages为扫描的路径，即com.deng.mapper
        //beanDefinitionHolders为spring框架帮我们扫描出来的bean定义对象，但属性不一定是正确的，需要我们修改
        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
            BeanDefinition beanDefinition = beanDefinitionHolder.getBeanDefinition();
            beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanDefinition.getBeanClassName());
            beanDefinition.setBeanClassName(DengFactoryBean.class.getName());
        }

        return beanDefinitionHolders;
    }
}
