package com.mvcframework.v2.context;

import com.mvcframework.v1.annotation.Autowired;
import com.mvcframework.v2.beans.BeanDefinition;
import com.mvcframework.v2.beans.BeanPostProcesser;
import com.mvcframework.v2.beans.BeanWrapper;
import com.mvcframework.v2.context.support.BeanDefinitionReader;
import com.mvcframework.v2.core.BeanFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: zhaomengjie
 * @Date: 2020/6/29 21:27
 * @Version 1.0
 */
public class GPApplicationContext implements BeanFactory {

    private String[] configLocations;

    private BeanDefinitionReader reader;

    //用来保存配置信息的
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

    //用来存储所有被保存过得对象
    private Map<String,BeanWrapper> beanWrapperMap = new HashMap<String, BeanWrapper>();

    //用来保证注册时单利
    private Map<String,Object> beanCacheMap = new HashMap<String, Object>();


    public GPApplicationContext(String...locations){
        this.configLocations = locations;
        this.refresh();
    }

    public void refresh(){

        //定位
        this.reader = new BeanDefinitionReader(configLocations);
        //加载
        List<String> beanDefinitions = reader.loadBeanDefinitions();
        //注册
        doRegisty(beanDefinitions);
        //依赖注入（lazy=false）要制定依赖注入，自动调用getBean方法，依赖注入是通过getBean来开始的
        doAutorited();
    }

    private void doAutorited() {
        for(Map.Entry<String,BeanDefinition> beanDefinitionEntry :this.beanDefinitionMap.entrySet()){
            String beanName = beanDefinitionEntry.getKey();
            if(!beanDefinitionEntry.getValue().isLazyInit()){
                getBean(beanName);
            }
        }

        for(Map.Entry<String,BeanWrapper> beanDefinitionEntry :this.beanWrapperMap.entrySet()){
            populateBean(beanDefinitionEntry.getKey(),beanDefinitionEntry.getValue().getWrapperInstance());
        }

    }

    //将beanDefintion注册到ioc容器中BeanDefinitionMap
    private void doRegisty(List<String> beanDefinitions) {
        for(String className:beanDefinitions){
            try {
                Class<?> clazz = Class.forName(className);

                //如果是一个借口不可以进行实例化
                if(clazz.isInterface()){
                    continue;
                }
                BeanDefinition beanDefinition = reader.rigisterBean(className);
                if(beanDefinition!=null){
                    beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
                }

                //如果是个接口
                Class<?>[] interfaces = clazz.getInterfaces();
                for(Class<?> cla:interfaces){
                    //注意接口的时候也是方的实力
                    //如果是多个实现类就只能覆盖
                    beanDefinitionMap.put(cla.getName(),beanDefinition);
                }

                //到这里容器初始化完毕

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过读取BeanDefinition中的信息
     * 然后，通过反射机制创建一个示例并返回
     * spiring的做法是，不会把最原始的对象放出去，会用一个BeanWrapper来进行包装
     * 装饰器模式
     * 1、保留原来的oop
     * 2、需要对它进行扩展
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        String className = beanDefinition.getBeanClassName();
        try{

            //通知
            BeanPostProcesser beanPostProcesser = new BeanPostProcesser();

            Object instance = instantionBean(beanDefinition);
            if(instance==null){
                return null;
            }
            beanPostProcesser.postProcessBeforeInitialization(instance,beanName);
            BeanWrapper beanWrapper = new BeanWrapper(instance);
            beanWrapperMap.put(beanName,beanWrapper);
            beanPostProcesser.postProcessAfterInitialization(instance,beanName);

            //populateBean(beanName,instance);
            //自己留有可操作空间
            return beanWrapperMap.get(beanName).getWrapperInstance();

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //传一个BeanDefinition返回一个示例bean
    private Object instantionBean(BeanDefinition beanDefinition){
        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        try {

            //根据class才能确定是否有示例
            if(this.beanCacheMap.containsKey(className)){
                instance = this.beanCacheMap.get(className);
            }else{
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                beanCacheMap.put(className,instance);
            }
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void populateBean(String beanName,Object instance){
        Class clazz = instance.getClass();
        Field[] fileds = clazz.getDeclaredFields();
        for(Field field:fileds){
            if(field.isAnnotationPresent(Autowired.class)){
                Autowired autowired = field.getAnnotation(Autowired.class);
                String autoBeanName = autowired.value().trim();
                if(("").equals(autoBeanName)){
                    autoBeanName=field.getType().getName();
                }
                field.setAccessible(true);
                try {
                    field.set(instance,this.beanWrapperMap.get(autoBeanName).getWrapperInstance());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
