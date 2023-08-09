package com.deng.service;


import com.deng.spring.Component;
import com.deng.spring.ComponentScan;

@ComponentScan("com.deng.service")
public class AppConfig {


    // 为什么没加@Configuration的时候，事务不生效

  /*  public  JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }

    public 事务管理器 transactionManager(){
        事务管理器.setDataSource(dataSource())
    }*/

    /*
    * 1.从以上代码可以看出，如果不加@Configuration，事务管理器和JdbcTemplate的数据源是不同的
    * 2.真实的数据库连接conn是存储在线程ThreadLocal<Map<DataSource, conn>>，因为一个线程可能要求不同的数据源，这样当使用JdbcTemplate执行语句时，实际上
    *   拿到的并不是事务管理器创建的连接。
    * 3.如果加上@Configuration，则是使用同个数据源创建的连接
    * 原理：@Configuration，AppConfig为代理对象，不是基于AOP，而是基于动态代理（和@Lazy）一样，执行代理对象的逻辑的时候，实质上就会去执行代理对象获得dataSource方法
    * */


}
