package com.deng.spring.interfacePackage;

/**
 * @Author: Deng.
 * @Description:控制某个具体的bean的创建并对他做处理，在容器初始化时，需要记录下来
 * @Date Created in 2023 -08 -03 13:40
 * @Modified By:
 */
public interface BeanPostProcessor {
    public Object postProcessBeforeInitialization(String beanName, Object bean);
    public Object postProcessAfterInitialization(String beanName, Object bean);

}
