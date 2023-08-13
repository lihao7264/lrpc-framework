package com.atlihao.lrpc.framework.core.client;

import lombok.Data;
import lombok.ToString;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: rpc远程调用包装类
 * @Author: lihao726726
 * @CreateDate: 2023/8/10 8:18 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/10 8:18 下午
 * @Version: 1.0.0
 */
@Data
@ToString
public class RpcReferenceWrapper<T> {

    private Class<T> targetClass;

    private Map<String, Object> attatchments = new ConcurrentHashMap<>();

    public boolean isAsync() {
        Object r = attatchments.get("async");
        if (r == null || r.equals(false)) {
            return false;
        }
        return true;
    }

    public void setAsync(boolean async) {
        this.attatchments.put("async", async);
    }

    public String getUrl() {
        return String.valueOf(attatchments.get("url"));
    }

    public void setUrl(String url) {
        attatchments.put("url", url);
    }

    public String getServiceToken() {
        return String.valueOf(attatchments.get("serviceToken"));
    }

    public void setServiceToken(String serviceToken) {
        attatchments.put("serviceToken", serviceToken);
    }

    public String getGroup() {
        return String.valueOf(attatchments.get("group"));
    }

    public void setGroup(String group) {
        attatchments.put("group", group);
    }

    public Long getTimeout() {
        return Long.valueOf(String.valueOf(attatchments.get("timeOut")));
    }

    public void setTimeOut(Long timeOut) {
        attatchments.put("timeOut", timeOut);
    }

    /**
     * 失败重试次数
     */
    public int getRetry(){
        if(attatchments.get("retry")==null){
            return 0;
        }else {
            return (int) attatchments.get("retry");
        }
    }

    public void setRetry(int retry){
        this.attatchments.put("retry",retry);
    }

    /**
     * 设置容错策略
     *
     * @param tolerant
     */
    public void setTolerant(String tolerant){
        this.attatchments.put("tolerant",tolerant);
    }

}
