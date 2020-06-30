package com.mvcframework.v1.servlet;

import com.mvcframework.v1.annotation.Autowired;
import com.mvcframework.v1.annotation.Controller;
import com.mvcframework.v1.annotation.Service;
import com.mvcframework.v1.demo.controller.DemoAction;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: zhaomengjie
 * @Date: 2020/5/19 16:38
 * @Version 1.0
 * servelt生命周期
 * 1.加载和实例化
 *
 * 2.初始化
 *
 * 3.请求处理
 *
 * 4.服务终止
 */
public class ZDispatcherServlet extends HttpServlet {

    private Properties contextConfig = new Properties();

    private Map<String,Object> beanMap = new ConcurrentHashMap<String, Object>();

    private List<String> classNames = new ArrayList<String>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        //开始初始化进程
        //定位
        doLoadConfig(servletConfig.getInitParameter("contextConfigLocation"));
        //加载
        doScanner(contextConfig.getProperty("scanPackage"));
        //注册
        doRegister();
        //自动依赖注入
        doAutowired();

        DemoAction demoAction = (DemoAction)beanMap.get("demoAction");
        demoAction.query(null,null,"赵梦杰");

        //如果是springmvc会多一个HandlerMapping
        //将@RequestMapping中的url和Method关联上
        //以便于浏览器获得用户输入的url后，能够找到具体Method通过反射调用
        initHandlerMapping();
    }

    private void initHandlerMapping() {

    }

    private void doAutowired() {
        if(beanMap.size()==0){
            return;
        }
        for(Map.Entry<String,Object> entry:beanMap.entrySet()){
            Field[] fileds = entry.getValue().getClass().getDeclaredFields();
            for(Field field:fileds){
                if(field.isAnnotationPresent(Autowired.class)){
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    String beanName = autowired.value().trim();
                    if(("").equals(beanName)){
                        beanName=field.getType().getName();
                    }
                    field.setAccessible(true);
                    try {
                        field.set(entry.getValue(),beanMap.get(beanName));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }




    }

    private void doRegister() {
        if(classNames.isEmpty()){
            return;
        }
        for(String className:classNames){
            try {
                Class<?> clazz = Class.forName(className);
                //在spring中是多个子方法处理的
                if(clazz.isAnnotationPresent(Controller.class)){
                    //在spring中这里并不是实例化而是传入BeanDefinition
                    beanMap.put(lowerFirstCase(clazz.getSimpleName()),clazz.newInstance());
                }else if(clazz.isAnnotationPresent(Service.class)){

                    Service service = clazz.getAnnotation(Service.class);
                    String name = service.value();
                    if(("").equals(name)){
                        name=lowerFirstCase(clazz.getSimpleName());
                    }
                    beanMap.put(name,clazz.newInstance());

                    //如果是个接口
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for(Class<?> cla:interfaces){
                        //注意接口的时候也是方的实力
                        beanMap.put(cla.getName(),clazz.newInstance());
                    }

                }else{
                    continue;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void doScanner(String packageName) {
        URL url = this.getClass().getClassLoader().getResource("/"+packageName.replaceAll("\\.","/"));
        File file = new File(url.getFile());
        for(File file1:file.listFiles()){
            if(file1.isDirectory()){
                doScanner(packageName+"."+file1.getName());
            }else{
                classNames.add(packageName+"."+file1.getName().replace(".class",""));
            }
        }
    }

    private void doLoadConfig(String location) {
        //在spring是通过reader去查找定位
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(location.replace("classpath:",""));
        try {
            contextConfig.load(is);
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
    }

    private String lowerFirstCase(String str){
        char[] chars = str.toCharArray();
        chars[0]+=32;
        return String.valueOf(chars);
    }

}
