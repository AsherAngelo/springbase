package com.mvcframework.v2.beans;

/**
 * @Author: zhaomengjie
 * @Date: 2020/6/29 21:35
 * @Version 1.0
 */
public class BeanWrapper {
    private Object wrapperInstance;
    private Object originalInstance;

    public BeanPostProcesser getBeanPostProcesser() {
        return beanPostProcesser;
    }

    public void setBeanPostProcesser(BeanPostProcesser beanPostProcesser) {
        this.beanPostProcesser = beanPostProcesser;
    }

    private BeanPostProcesser beanPostProcesser;

    public BeanWrapper(Object originalInstance) {
        this.wrapperInstance = originalInstance;
        this.originalInstance = originalInstance;
    }

    public Object getWrapperInstance() {
        return wrapperInstance;
    }

    public void setWrapperInstance(Object wrapperInstance) {
        this.wrapperInstance = wrapperInstance;
    }

    public Object getOriginalInstance() {
        return originalInstance;
    }

    public void setOriginalInstance(Object originalInstance) {
        this.originalInstance = originalInstance;
    }

    //返回代理后的class,就是以后的$Proxy0
    public Class<?> getWrappedClass(){
        return wrapperInstance.getClass();
    }
}
