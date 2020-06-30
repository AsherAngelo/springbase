package com.mvcframework.v1.demo.controller;

import com.mvcframework.v1.annotation.Autowired;
import com.mvcframework.v1.annotation.Controller;
import com.mvcframework.v1.annotation.RequestMapping;
import com.mvcframework.v1.annotation.RequestParam;
import com.mvcframework.v1.demo.service.IDemoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.IdentityHashMap;

/**
 * @Author: zhaomengjie
 * @Date: 2020/5/19 22:54
 * @Version 1.0
 */
@Controller
public class DemoAction {

    @Autowired
    private IDemoService demoService;

    @RequestMapping("/123")
    public void query(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestParam String name){
        String result = demoService.query(name);
        try {
            httpServletResponse.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
