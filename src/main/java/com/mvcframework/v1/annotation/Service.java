package com.mvcframework.v1.annotation;

import java.lang.annotation.*;

/**
 * @Author: zhaomengjie
 * @Date: 2020/5/19 22:50
 * @Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value() default "";
}
