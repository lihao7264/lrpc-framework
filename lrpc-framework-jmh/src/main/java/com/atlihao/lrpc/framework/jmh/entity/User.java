package com.atlihao.lrpc.framework.jmh.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/10 7:19 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/10 7:19 下午
 * @Version: 1.0.0
 */
@Data
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = 1181291230299531326L;

    private Integer id;

    private String username;

    private String idCardNo;

    private String tel;

    private Integer age;

    private Integer sex;

    private Long bankNo;

    private String address;

    private String remark;
}
