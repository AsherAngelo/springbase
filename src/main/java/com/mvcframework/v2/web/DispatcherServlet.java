package com.mvcframework.v2.web;

import com.mvcframework.v2.context.GPApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: zhaomengjie
 * @Date: 2020/6/29 21:26
 * @Version 1.0
 */

//只作为入口
public class DispatcherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        GPApplicationContext gpApplicationContext = new GPApplicationContext(servletConfig.getInitParameter("contextConfigLocation"));
    }
}
