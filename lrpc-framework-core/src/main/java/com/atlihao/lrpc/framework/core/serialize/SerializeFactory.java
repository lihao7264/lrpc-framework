package com.atlihao.lrpc.framework.core.serialize;

/**
 * @Description: 序列化工厂
 * @Author: lihao726726
 * @CreateDate: 2023/8/10 7:23 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/10 7:23 下午
 * @Version: 1.0.0
 */
public interface SerializeFactory {

    /**
     * 序列化
     *
     * @param t
     * @param <T>
     * @return
     */
    <T> byte[] serialize(T t);

    /**
     * 反序列化
     *
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] data, Class<T> clazz);
}
