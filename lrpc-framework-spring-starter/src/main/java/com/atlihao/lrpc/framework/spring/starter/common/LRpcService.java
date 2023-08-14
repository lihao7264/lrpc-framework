package com.atlihao.lrpc.framework.spring.starter.common;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 1:22 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 1:22 下午
 * @Version: 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface LRpcService {

    int limit() default 0;

    String group() default "default";

    String serviceToken() default "";

}
