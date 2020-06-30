package com.mvcframework.v2.annotation;

import java.lang.annotation.*;

/**
 * @Author: zhaomengjie
 * @Date: 2020/5/19 22:51
 * @Version 1.0
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    String value() default "";
}
