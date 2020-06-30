package com.mvcframework.v1.demo.service.impl;

import com.mvcframework.v1.annotation.Service;
import com.mvcframework.v1.demo.service.IDemoService;

/**
 * @Author: zhaomengjie
 * @Date: 2020/5/19 22:56
 * @Version 1.0
 */
@Service
public class DemoService implements IDemoService {
    public String query(String name) {
        System.out.println("你好："+name) ;
        return name;
    }
}
