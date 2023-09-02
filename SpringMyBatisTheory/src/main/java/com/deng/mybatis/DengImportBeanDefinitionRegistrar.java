package com.deng.mybatis;


import com.deng.mapper.UserMapper;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.Map;



//当spring检测到bean对象实现了ImportBeanDefinitionRegistrar接口，就会去执行registerBeanDefinitions方法
public class DengImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {

        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(DengMapScan.class.getName());
        String path = (String) annotationAttributes.get("value");//获得mapper路径

        DengScanner dengScanner = new DengScanner(registry); //扫描器，用于扫描路径并加载得到对应的beanDefinition对象
        dengScanner.addIncludeFilter(new TypeFilter() {
            @Override
            public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                return true;
            }
        });

        dengScanner.scan(path);

    }

    /*
    原本的方法，只能写死需要生成哪个接口的mapper，不利于扩展

    @Override
    public void registerBeanDefinitions1(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();//获得bean对象定义
        beanDefinition.setBeanClass(DengFactoryBean.class);//不能写UserMapper.class，接口不能成为对象
        beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(UserMapper.class);//传入构造方法的参数


        //registry就代表着spring容器
        registry.registerBeanDefinition("userMapper", beanDefinition);//放定义放入map中
    }

        */
}
