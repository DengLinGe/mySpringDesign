package com.deng.spring.interfacePackage;

/**
 * @Author: Deng.
 * @Description: 用于告诉程序员bean的名字是什么，通过接口的回调
 * @Date Created in 2023 -08 -03 13:29
 * @Modified By:
 */
public interface BeanNameAware {
    public void setBeanName(String beanName);
}
