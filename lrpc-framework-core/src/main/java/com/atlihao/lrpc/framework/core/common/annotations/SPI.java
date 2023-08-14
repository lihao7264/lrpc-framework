package com.atlihao.lrpc.framework.core.common.annotations;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/12 8:15 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/12 8:15 下午
 * @Version: 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SPI {

    String value() default "";

}
