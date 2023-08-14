package com.atlihao.lrpc.framework.spring.starter.common;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 1:22 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 1:22 下午
 * @Version: 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LRpcReference {

    String url() default "";

    String group() default "default";

    String serviceToken() default "";

    long timeOut() default 3000L;

    int retry() default 1;

    boolean async() default false;

}
