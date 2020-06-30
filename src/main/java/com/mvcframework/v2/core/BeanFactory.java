package com.mvcframework.v2.core;

/**
 * @Author: zhaomengjie
 * @Date: 2020/6/29 21:28
 * @Version 1.0
 */
public interface BeanFactory {
    //从ioc容器中回去实例bean
    Object getBean(String beanName);
}
