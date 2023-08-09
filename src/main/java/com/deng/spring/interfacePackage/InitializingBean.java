package com.deng.spring.interfacePackage;

/**
 * @Author: Deng.
 * @Description: 初始化bean的操作
 * @Date Created in 2023 -08 -03 13:35
 * @Modified By:
 */
public interface InitializingBean {
    public void  afterPropertiesSet();
}
