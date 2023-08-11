package com.atlihao.lrpc.framework.core.server;

import lombok.Data;
import lombok.ToString;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/10 8:38 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/10 8:38 下午
 * @Version: 1.0.0
 */
@Data
@ToString
public class ServiceWrapper {

    /**
     * 对外暴露的具体服务对象
     */
    private Object serviceBean;

    /**
     * 具体暴露服务的分组
     */
    private String group = "default";

    public ServiceWrapper(Object serviceBean) {
        this.serviceBean = serviceBean;
    }

    public ServiceWrapper(Object serviceBean, String group) {
        this.serviceBean = serviceBean;
        this.group = group;
    }

}
