package com.deng.service;

import com.deng.spring.Autowired;
import com.deng.spring.Component;

/**
 * @Author: Deng.
 * @Description:
 * @Date Created in 2023 -08 -06 11:49
 * @Modified By:
 */
@Component
public class AService {
    @Autowired
    public BService bService;
    //三级缓存
    /*
     * 第一级缓存：singletonObjects(单例池)
     * 第二级缓存: earlySingletonObjects：存储提早出现的bean对象，没有出现完整的生命周期，出现循环依赖的时候使用
     * 第三级缓存: singletonFactories：存储的是lambda表达式，即普通对象
     * （第二第三级缓存是互斥的，一个存另一个就要删）
     *
     * */

    //！！！！！第一阶段分析：分析依赖循环，需要先从bean的生命周期中去分析
    /*
    * AService的Bean的生命周期
        1.实例化-->普通对象
        2.填充bService--->单例池Map--->创建BService
                BService的Bean的生命周期
                    2.1.实例化-->普通对象
                    2.2.填充aService--->单例池Map-->creatingSet->出现循环依赖->进行aop创建AService的代理对象--->再赋值到BService属性
                            *出现依赖......（怎么判断出现依赖-creatingSet：指的是当前的正在创建的bean对象，如果创建完就移除）
                    2.3.填充其他属性
                    2.4.做一些其他的事情AOP
                    2.5.添加到单例池
        3 .填充其他属性
        *4.做一些其他的事情(AoP)
        5.添加到单例池
    * */


    //解决方案
    /*
    * 1.增加一个map，在对象实例化的时候就把普通对象放入这个map，这样在Bservice填充AService的时候就可以直接从map中取
    * 2.（针对1的弊端：如果此时启动aop，放入Map的应该是代理对象）-》提前进行aop，在实例化后就进行。（出现的循环依赖才会出现提前aop，需要提前判断）
    * 3.通过creatingSet进行判断是否有循环依赖，若有则提前aop
    * 弊端：什么时候将AService的代理对象放入单例池中呢，如果是aop完就放入，此时如果有两个属性都发生循环依赖怎么办
    * */


    //！！！！！第二阶段分析：二级缓存，用多一个map缓存代理对象
    /*
   AService的Bean的生命周期
        1.实例化-->普通对象
        2.填充bService--->单例池Map--->创建BService
                BService的Bean的生命周期
                    2.1.实例化-->普通对象
                    2.2.填充aService--->单例池Map-->creatingSet->出现循环依赖->从earlySingletonObjects看是否曾经创建---->(进行aop创建AService的代理对象--->再赋值到BService属性--->将代理对象放入earlySingletonObjects）
                            *出现依赖......（怎么判断出现依赖-creatingSet：指的是当前的正在创建的bean对象，如果创建完就移除）
                    2.3.填充其他属性
                    2.4.做一些其他的事情AOP
                    2.5.添加到单例池
        3 .填充其他属性
        *4.做一些其他的事情(AoP)
        *从earlySingletonObjects.get（刚刚创建的代理对象），将普通对象赋值给代理对象
        5.添加到单例池
    * */
    //弊端：在进行aop的时候，是需要拿到普通对象的，那么从哪里可以拿到普通对象？（如果直接实例化，又会重新进入到新的循环依赖）






    //！！！！！第三阶段分析：三级缓存，使用singletonFactories存储
    /*
     1.实例化-->AService普通对象->（判断支不支持循环依赖、是不是单例bean）singletonFactories.put('AService', （）-> getEarlyBeanReference(beanName, mbd, AService普通对象))，存的是lamda表达式
        2.填充bService--->单例池Map--->创建BService
                BService的Bean的生命周期
                    2.1.实例化-->普通对象
                    2.2.填充aService--->单例池Map-->creatingSet->出现循环依赖->从earlySingletonObjects看是否曾经创建---->
                        (二级缓存找不到，进行aop创建AService的代理对象-->从三级缓存找，通过singletonFactories拿到lambda表达式并执行得到aop对象--->再赋值到BService属性--->将代理对象放入earlySingletonObjects）
                                    *出现依赖......（怎么判断出现依赖-creatingSet：指的是当前的正在创建的bean对象，如果创建完就移除）
                    2.3.填充其他属性
                    2.4.做一些其他的事情AOP
                    2.5.添加到单例池
        3 .填充其他属性
        *4.做一些其他的事情(AoP)：首先判断earlyProxyReference有没有这个代理对象，没有则创建aop，有就获取并移除aop
        *从earlySingletonObjects.get（刚刚创建的代理对象），将普通对象赋值给代理对象
        5.添加到单例池
    * */



}
