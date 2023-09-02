package com.deng;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Deng.
 * @Description:
 * @Date Created in 2023 -08 -09 20:11
 * @Modified By:
 */

@Configuration
@ComponentScan("com.deng")
public class AppConfig {
    /*
    * 0.源码入口：doProcessConfigurationClass()
    * 1.扫描实际生成的是beanDefinition对象->Set<BeanDefinitionHolder> scannedBeanDefinitions
    * 2.可以配置多个ComponentScan，源码中
    *       Set<AnnotationAttributes> componentScans = AnnotationConfigUtils.attributesForRepeatable(
				sourceClass.getMetadata(), ComponentScans.class, ComponentScan.class);
    * 3.解析后会去看BeanDefinition是不是配置类，如果是配置类则要把配置类同样生成BeanDefinition
    *   怎么判断？通过isConfigurationCandidate()方法进行判断，只要存在@Component、@ComponentScan、@Import、@ImportResource四个中的一个，就是lite配置类
    *   同时如果类里面方法有@Bean注解，也会是配置类
    *
    * 4.启动扫描后，怎么解析ComponentScan注解
        Set<BeanDefinitionHolder> scannedBeanDefinitions = this.componentScanParser.parse(componentScan, sourceClass.getMetadata().getClassName());
        1)构造一个扫描器，并设置相关参数
        2)componentScan.getClass("nameGenerator"); 获取bean名字生成器属性，获取每个bean的名字，并通过规则进行转换
        3)componentScan.getEnum("scopedProxy");  获得ScopeProxy属性，用于批量控制扫描路径下bean什么时候开始加载
        4) 相当于把@componentScan所有的属性都设置进去扫描器
        5)扫描的时候，如果没有给出路径，则会默认扫描声明类（即开启扫描的AppConfig）所在的包
        6)AppConfig开始扫描时，一开始就会注册成bean，所以最后扫描时会排除AppConfig
           	scanner.addExcludeFilter(new AbstractTypeHierarchyTraversingFilter(false, false) {
			@Override
			protected boolean matchClassName(String className) {
				return declaringClass.equals(className);
			}
		});

	    7）扫描后生成beanDefinition对象，然后会进行查重
    * 5.spring利用ASM技术解析类原因：
        1)  如果加载时将所有的字节码都加载进jvm再读取类的信息会导致做很多无用功
        2） asm是字节码增强技术，通过asm可以生成新的class文件，也可以动态的修改即将要装载入jvm的类信息。
    * 6.重复扫描的bean定义会去检查是否重复：checkCandidate(String beanName, BeanDefinition beanDefinition)


    * */
    //



}
