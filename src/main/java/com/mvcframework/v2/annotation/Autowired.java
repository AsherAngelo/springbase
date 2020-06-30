package com.mvcframework.v2.annotation;

import java.lang.annotation.*;

/**
 * @Author: zhaomengjie
 * @Date: 2020/5/19 22:52
 * @Version 1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    String value() default "";
}
