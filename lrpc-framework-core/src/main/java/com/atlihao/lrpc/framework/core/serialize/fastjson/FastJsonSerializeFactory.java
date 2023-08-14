package com.atlihao.lrpc.framework.core.serialize.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atlihao.lrpc.framework.core.serialize.SerializeFactory;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/10 7:26 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/10 7:26 下午
 * @Version: 1.0.0
 */
public class FastJsonSerializeFactory implements SerializeFactory {

    /**
     * 序列化
     *
     * @param t
     * @return
     */
    @Override
    public <T> byte[] serialize(T t) {
        String result = JSON.toJSONString(t);
        return result.getBytes();
    }

    /**
     * 反序列化
     *
     * @param data
     * @param clazz
     * @return
     */
    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSONObject.parseObject(new String(data), clazz);
    }
}
