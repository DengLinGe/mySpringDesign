package com.deng.service;

import com.deng.service.aopInterface.UserInterface;
import com.deng.spring.Autowired;
import com.deng.spring.interfacePackage.BeanNameAware;
import com.deng.spring.Component;
import com.deng.spring.Scope;
import com.deng.spring.interfacePackage.InitializingBean;

@Component("userService")
@Scope("propotype")
public class UserService implements BeanNameAware, InitializingBean, UserInterface {

    @Autowired
    private OrderService orderService;


    //注意，autowired和构造器注入先bytype再byname


    //执行事务的顺序，以jdbc为例子
    /*
    * 1.spring的事务管理器新建一个数据库连接conn
    * 2.将conn.autocommit = false，如果为true，则每执行一个语句都会自动commit
    * 3.jdbctemplate会拿到数据库连接conn去执行bean对象中的相关方法语句
    * */


    // 为什么在事务方法中调用类内另一个事务方法，没办法开启事务
    /*
    * 1.代理类在处理out（）时，会有额外的逻辑处理@Transactional注解，然后再调用普通对象的out()
    * 2.普通对象本身不具备处理@Transactional的能力，而在out调用时，实际上是调用了普通对象的out，也就是从out（）中调用了普通对象的in（）
    * */

//    @Transactional
    public void out(){
        in();
    }
    //    @Transactional
    public void in(){

    }




    @Override
    public void test(){
        System.out.println(orderService);
    }

    // 不是由程序员去掉，而是由spring去调用，拿到这个名字去干什么就由程序员决定
    @Override
    public void setBeanName(String beanName) {
        ////做一些操作
        System.out.println("这是UserService:SetBeanName方法");
    }



    @Override
    public void afterPropertiesSet() {
        System.out.println("这是UserService:afterPropertiesSet方法");
    }
}
