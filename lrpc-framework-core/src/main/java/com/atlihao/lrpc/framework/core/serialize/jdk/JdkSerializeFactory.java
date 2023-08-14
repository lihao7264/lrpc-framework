package com.atlihao.lrpc.framework.core.serialize.jdk;

import com.atlihao.lrpc.framework.core.serialize.SerializeFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/10 7:30 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/10 7:30 下午
 * @Version: 1.0.0
 */
public class JdkSerializeFactory implements SerializeFactory {
    /**
     * 序列化
     *
     * @param t
     * @return
     */
    @Override
    public <T> byte[] serialize(T t) {
        byte[] data = null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(os);
            output.writeObject(t);
            output.flush();
            output.close();
            data = os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return data;
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
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        try {
            ObjectInputStream input = new ObjectInputStream(inputStream);
            Object result = input.readObject();
            return (T) result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
