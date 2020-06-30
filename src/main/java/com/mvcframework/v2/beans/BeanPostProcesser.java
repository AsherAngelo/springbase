package com.mvcframework.v2.beans;

import com.mvcframework.v2.core.FactoryBean;

/**
 * @Author: zhaomengjie
 * @Date: 2020/6/29 23:39
 * @Version 1.0
 */
public class BeanPostProcesser implements FactoryBean {

    public Object postProcessBeforeInitialization(Object bean, String beanName){
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
