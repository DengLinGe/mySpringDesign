package com.deng;

import com.deng.mybatis.DengImportBeanDefinitionRegistrar;
import com.deng.mybatis.DengMapScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author: Deng.
 * @Description:
 * @Date Created in 2023 -08 -06 21:28
 * @Modified By:
 */

@Configuration
@ComponentScan("com.deng")
@DengMapScan("com.deng.mapper")
//@Import(DengImportBeanDefinitionRegistrar.class)
public class AppConfig {
}
