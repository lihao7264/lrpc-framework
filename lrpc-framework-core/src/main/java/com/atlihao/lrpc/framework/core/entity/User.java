package com.atlihao.lrpc.framework.core.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Description: 测试自定义序列化技术时使用的demo
 * @Author: lihao726726
 * @CreateDate: 2023/8/10 7:24 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/10 7:24 下午
 * @Version: 1.0.0
 */
@Data
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = -7542175520317532250L;

    private Integer id;

    private Long tel;
}
