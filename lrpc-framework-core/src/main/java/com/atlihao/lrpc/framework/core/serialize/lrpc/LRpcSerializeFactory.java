package com.atlihao.lrpc.framework.core.serialize.lrpc;

import com.atlihao.lrpc.framework.core.entity.User;
import com.atlihao.lrpc.framework.core.serialize.SerializeFactory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/10 7:42 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/10 7:42 下午
 * @Version: 1.0.0
 */
public class LRpcSerializeFactory implements SerializeFactory {

    @AllArgsConstructor
    @Data
    class ByteHolder {
        private byte[] bytes;
    }

    /**
     * 序列化(将对象中的数据全部 转换为 byte[])
     *
     * @param t
     * @return
     */
    @Override
    public <T> byte[] serialize(T t) {
        Field[] declaredFields = t.getClass().getDeclaredFields();
        List<ByteHolder> byteHolderList = new ArrayList<>();
        int totalSize = 0;
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(Boolean.TRUE);
            try {
                Object orgVal = declaredField.get(t);
                byte[] arr = this.getByteArrayByField(declaredField, orgVal);
                totalSize += arr.length;
                byteHolderList.add(new ByteHolder(arr));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 结果 byte[]
        byte[] result = new byte[totalSize];
        int index = 0;
        for (ByteHolder byteHolder : byteHolderList) {
            System.arraycopy(byteHolder.getBytes(), 0, result, index, byteHolder.getBytes().length);
            index += byteHolder.getBytes().length;
        }
        return result;
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
        return null;
    }

    /******************************** 内部私有函数 ********************************/
    public byte[] getByteArrayByField(Field field, Object orgVal) {
        Class type = field.getType();
        if ("java.lang.Integer".equals(type.getName())) {
            return ByteConvertUtils.intToByte((Integer) orgVal);
        } else if ("java.lang.Long".equals(type.getName())) {
            return ByteConvertUtils.longToByte((Long) orgVal);
        } else if ("java.lang.Short".equals(type.getName())) {
            return ByteConvertUtils.shortToByte((Short) orgVal);
        } else if ("java.lang.String".equals(type.getName())) {
            if (orgVal == null) {
                return new byte[0];
            }
            return ((String) orgVal).getBytes();
        }
        return new byte[0];
    }


    /**
     * User 转为 byte[]
     * System.arraycopy解析
     * src：byte源数组
     * srcPos：截取源byte数组起始位置（0位置有效）
     * dest：byte目的数组（截取后存放的数组）
     * destPos：截取后存放的数组起始位置（0位置有效）
     * length：截取的数据长度
     */
    private byte[] userToByteArray(User user) {
        byte[] data = new byte[Integer.BYTES + Long.BYTES];
        int index = 0;
        System.arraycopy(ByteConvertUtils.intToByte(user.getId()), 0, data, index, Integer.BYTES);
        index += Integer.BYTES;
        System.arraycopy(ByteConvertUtils.longToByte(user.getTel()), 0, data, index, Long.BYTES);
        return data;
    }


    /**
     * byte[] 转为 User
     *
     * @param bytes
     * @return
     */
    private User byteArrayToUser(byte[] bytes) {
        User user = new User();
        byte[] idBytes = new byte[Integer.BYTES];
        byte[] telBytes = new byte[Long.BYTES];
        System.arraycopy(bytes, 0, idBytes, 0, Integer.BYTES);
        System.arraycopy(bytes, Integer.BYTES, telBytes, 0, Long.BYTES);
        int id = ByteConvertUtils.byteToInt(idBytes);
        long tel = ByteConvertUtils.byteToLong(telBytes);
        user.setId(id);
        user.setTel(tel);
        return user;
    }

    public static void main(String[] args) {
        LRpcSerializeFactory serializeFactory = new LRpcSerializeFactory();
        User user = new User();
        user.setId(11);
        user.setTel(12L);
        byte[] r = serializeFactory.serialize(user);
        System.out.println(r.length);
        User user1 = serializeFactory.byteArrayToUser(r);
        System.out.println(user1);
    }
}
