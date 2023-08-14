package com.atlihao.lrpc.framework.jmh.serialize;

import com.atlihao.lrpc.framework.core.serialize.SerializeFactory;
import com.atlihao.lrpc.framework.core.serialize.fastjson.FastJsonSerializeFactory;
import com.atlihao.lrpc.framework.core.serialize.hessian.HessianSerializeFactory;
import com.atlihao.lrpc.framework.core.serialize.jdk.JdkSerializeFactory;
import com.atlihao.lrpc.framework.core.serialize.kryo.KryoSerializeFactory;
import com.atlihao.lrpc.framework.jmh.entity.User;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/10 7:21 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/10 7:21 下午
 * @Version: 1.0.0
 */
public class SerializeByteSizeCompareTest {

    private static User buildUserDefault() {
        User user = new User();
        user.setAge(26);
        user.setAddress("杭州市西湖区");
        user.setBankNo(123456789L);
        user.setSex(1);
        user.setId(123456);
        user.setIdCardNo("330825199702107777");
        user.setRemark("测试");
        user.setUsername("天天帝天");
        return user;
    }

    public void jdkSerializeSizeTest() {
        SerializeFactory serializeFactory = new JdkSerializeFactory();
        User user = buildUserDefault();
        byte[] result = serializeFactory.serialize(user);
        System.out.println("jdk's size is " + result.length);
    }

    public void hessianSerializeSizeTest() {
        SerializeFactory serializeFactory = new HessianSerializeFactory();
        User user = buildUserDefault();
        byte[] result = serializeFactory.serialize(user);
        User deserializeUser = serializeFactory.deserialize(result, User.class);
        System.out.println("hessian's size is " + result.length);
    }

    public void fastJsonSerializeSizeTest() {
        SerializeFactory serializeFactory = new FastJsonSerializeFactory();
        User user = buildUserDefault();
        byte[] result = serializeFactory.serialize(user);
        User deserializeUser = serializeFactory.deserialize(result, User.class);
        System.out.println("fastJson's size is " + result.length);
    }

    public void kryoSerializeSizeTest() {
        SerializeFactory serializeFactory = new KryoSerializeFactory();
        User user = buildUserDefault();
        byte[] result = serializeFactory.serialize(user);
        User deserializeUser = serializeFactory.deserialize(result, User.class);
        System.out.println("kryo's size is " + result.length);
    }

    /**
     * fastJson's size is 156
     * jdk's size is 443
     * kryo's size is 120  （字节数最小）
     * hessian's size is 175
     *
     * @param args
     */
    public static void main(String[] args) {
        SerializeByteSizeCompareTest serializeByteSizeCompareTest = new SerializeByteSizeCompareTest();
        serializeByteSizeCompareTest.fastJsonSerializeSizeTest();
        serializeByteSizeCompareTest.jdkSerializeSizeTest();
        serializeByteSizeCompareTest.kryoSerializeSizeTest();
        serializeByteSizeCompareTest.hessianSerializeSizeTest();
    }
}
