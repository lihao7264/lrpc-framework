package com.atlihao.lrpc.framework.core.serialize.hessian;

import com.atlihao.lrpc.framework.core.serialize.SerializeFactory;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/10 7:28 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/10 7:28 下午
 * @Version: 1.0.0
 */
public class HessianSerializeFactory implements SerializeFactory {
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
            Hessian2Output output = new Hessian2Output(os);
            output.writeObject(t);
            output.getBytesOutputStream().flush();
            output.completeMessage();
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
        if (data == null) {
            return null;
        }
        Object result = null;
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            Hessian2Input input = new Hessian2Input(is);
            result = input.readObject(clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return (T) result;
    }

}
