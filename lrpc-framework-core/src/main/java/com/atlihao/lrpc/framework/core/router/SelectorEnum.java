package com.atlihao.lrpc.framework.core.router;

import lombok.Data;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/10 9:23 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/10 9:23 上午
 * @Version: 1.0.0
 */
public enum SelectorEnum {

    RANDOM_SELECTOR(0, "random");

    int code;
    String desc;

    private SelectorEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
