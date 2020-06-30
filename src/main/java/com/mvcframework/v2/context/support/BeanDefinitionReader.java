package com.mvcframework.v2.context.support;

import com.mvcframework.v2.beans.BeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @Author: zhaomengjie
 * @Date: 2020/6/29 21:34
 * @Version 1.0
 */
//对配置文件进行查找、读取、解析
public class BeanDefinitionReader {

    private Properties config = new Properties();

    private List<String> registerClassName = new ArrayList<String>();

    private static final String SCAN_PACKAGE = "scanPackage";

    public BeanDefinitionReader(String...locations) {
        //在spring是通过reader去查找定位
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:",""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    public List<String> loadBeanDefinitions(){
        return registerClassName;
    }

    //每注册一个className，就返回一个BeanDefinition,我们自己包装，我们对配置信息进行一个包装
    public BeanDefinition rigisterBean(String className){
        if(this.registerClassName.contains(className)){
            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setBeanClassName(className);
            beanDefinition.setFactoryBeanName(lowerFirstCase(className));
        }
        return null;
    }

    public Properties getConfig() {
        return config;
    }

    private void doScanner(String packageName) {
        URL url = this.getClass().getClassLoader().getResource("/"+packageName.replaceAll("\\.","/"));
        File file = new File(url.getFile());
        for(File file1:file.listFiles()){
            if(file1.isDirectory()){
                doScanner(packageName+"."+file1.getName());
            }else{
                registerClassName.add(packageName+"."+file1.getName().replace(".class",""));
            }
        }
    }

    private String lowerFirstCase(String str){
        char[] chars = str.toCharArray();
        chars[0]+=32;
        return String.valueOf(chars);
    }


}
