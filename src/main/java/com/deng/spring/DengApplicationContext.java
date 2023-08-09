package com.deng.spring;

import com.deng.spring.interfacePackage.BeanNameAware;
import com.deng.spring.interfacePackage.BeanPostProcessor;
import com.deng.spring.interfacePackage.InitializingBean;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class DengApplicationContext {
        private Class configClass;
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(); // 用于存储扫描生成的bean定义对象

        private ConcurrentHashMap<String, Object> singletonObjectMap = new ConcurrentHashMap<>(); // 单例池
        private ArrayList<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();// 记录有多少个bean 后置处理器
    public DengApplicationContext(Class configClass) {
        this.configClass = configClass;


        /*
        *扫描所有类并加载进bean
        * 1. 拿到扫描路径，定义在AppConfig上，将所有bean信息加载到map中
        * 2. 实例所有的单例bean
        * */

        if (configClass.isAnnotationPresent(ComponentScan.class)) { // 获取到注解的信息
            ComponentScan componentScanAnnotation = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String scanPath = componentScanAnnotation.value(); // 拿到扫描路径 com.deng.service

            String path = scanPath.replace(".", "/");

            // 寻找target中的class，而不是源代码的class
            ClassLoader classLoader = DengApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path); // 取出来target中的真实路径


            File file = new File(resource.getFile());


            if (file.isDirectory()){
                File[] files = file.listFiles();
                for (File f : files) {
                    String fileName = f.getAbsolutePath(); // 获得每个class的绝对路径

                    if (fileName.endsWith(".class")){ // 只判断.class是不是一个bean
                        /*
                        用反射判断该class有没有Component注解
                        * */
                        // 原始格式：com\deng\service\XXX.class
                        String classNameBefore = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                        String className = classNameBefore.replace("\\", ".");


                        try {
                            Class<?> clazz = classLoader.loadClass(className);
                            if (clazz.isAnnotationPresent(Component.class)){

                                if(BeanPostProcessor.class.isAssignableFrom(clazz)){
                                    //判断这个类有没有实现这个接口。如果有就生成对象
                                    BeanPostProcessor instance = (BeanPostProcessor)clazz.newInstance();
                                    beanPostProcessorList.add(instance);
                                }




                                /*
                                * 判断是否带有注解，但不应该直接创建出来，因为有单例bean和多例bean的区别
                                * 现在要先生成beandefinition对象
                                * */
                                BeanDefinition beanDefinition = new BeanDefinition();
                                beanDefinition.setType(clazz);

                                if (clazz.isAnnotationPresent(Scope.class)){
                                    Scope scopeAnnotation = clazz.getAnnotation(Scope.class);
                                    String scope = scopeAnnotation.value();
                                    beanDefinition.setScope(scope);
                                }else {//不含Scope注解，即按照默认方式进行
                                    beanDefinition.setScope("singleton");
                                }


                                /*
                                * 将beanDefinition对象放入map中
                                * 1.先获得bean的名字,如果为空，则需要给默认值
                                * 2.根据名字的填写值放入map
                                * */
                                Component componentAnnotation = clazz.getAnnotation(Component.class);
                                String beanName = null;
                                if ("".equals(componentAnnotation.value())){
                                    String simpleName = clazz.getSimpleName();
                                    String firstChar = simpleName.substring(0, 1).toLowerCase();
                                    String restChars = simpleName.substring(1);
                                    beanName = firstChar + restChars;

                                }else {
                                    beanName = componentAnnotation.value();
                                }
                                beanDefinitionMap.put(beanName, beanDefinition);
                            }

                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (InstantiationException e) {
                            throw new RuntimeException(e);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }


                    }

                }
            }


        }

        /*扫描map,创建单例bean对象，存储起来*/
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if ("singleton".equals(beanDefinition.getScope())){
                Object bean = createBean(beanName, beanDefinition);
                singletonObjectMap.put(beanName, bean);
            }
        }
    }



    private Object createBean(String beanName, BeanDefinition beanDefinition){
        Class clazz = beanDefinition.getType();// 拿到bean的类信息

        try {
            /*
            * 模拟bean的声明周期
            * 1.创建出对象的时候，同时需要把里面的autowire对象给依赖注入
            * 2.不加autowired的不需要
            * 3.根据bean实现的接口初始化bean
            * */
            Object instance = clazz.getConstructor().newInstance();

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)){
                    /*
                    * 1.判断属性是否存在autowired注解
                    * 2.判断是否是单例模式，如果是则是否之前已经创建过在单例池中
                    * */
                    field.setAccessible(true);
                    Object bean = getBean(field.getName());
                    field.set(instance, bean);
                }
            }

            //spring只关心有没有，有就调用，但不关心实现
            if (instance instanceof BeanNameAware){ //判断对象有没有实现beanName的接口，若有则需要回调
                ((BeanNameAware) instance).setBeanName(beanName);
            }
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                beanPostProcessor.postProcessBeforeInitialization(beanName, instance);
            }

            if (instance instanceof InitializingBean){ //判断对象有没有实现InitializingBean的接口，需要初始化，若有则需要回调
                ((InitializingBean) instance).afterPropertiesSet();
            }

            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                instance = beanPostProcessor.postProcessAfterInitialization(beanName, instance);
            }

            // 返回对象，但应该返回的是aop的代理对象
            return instance;


        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    /*
     * @description TODO
     * @param beanName        
     * @return java.lang.Object
     * @author EdisonChen
     * @date 2023/8/3 11:18
    */
    public Object getBean(String beanName){
        // 需要判断单例还是多例，从map中获得bean对象

        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);

        if (beanDefinition == null){// 如果map中找不到，则需要抛出异常
            throw new NullPointerException(String.format("无法查询到该bean：%s", beanName));
        }else {
            // 观察他的作用域
            String scope = beanDefinition.getScope();

            if (scope.equals("singleton")){
                // 单例，程序只允许有一个
                Object bean = singletonObjectMap.get(beanName);
                if (bean == null){ //在单例池子里没找到，就需要重新创建，并放入池子里
                    Object o = createBean(beanName, beanDefinition);
                    singletonObjectMap.put(beanName, o);
                    bean = o;
                }
                return bean;
            }else {
                // 多例，需要每次用都要创建一个
                return createBean(beanName, beanDefinition);
            }
        }

    }
}
