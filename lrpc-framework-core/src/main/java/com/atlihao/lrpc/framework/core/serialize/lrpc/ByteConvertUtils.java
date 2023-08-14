package com.atlihao.lrpc.framework.core.serialize.lrpc;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.List;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/10 7:45 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/10 7:45 下午
 * @Version: 1.0.0
 */
public class ByteConvertUtils {

    /**
     * int 转为 byte[]
     *
     * @param n
     * @return
     */
    public static byte[] intToByte(int n) {
        byte[] buf = new byte[4];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) (n >> (8 * i));
        }
        return buf;
    }

    /**
     * short 转为 byte[]
     *
     * @param n
     * @return
     */
    public static byte[] shortToByte(short n) {
        byte[] buf = new byte[2];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) (n >> (8 * i));
        }
        return buf;
    }

    /**
     * long 转为 byte[]
     *
     * @param j
     * @return
     */
    public static byte[] longToByte(long j) {
        byte[] buf = new byte[8];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) (j >> 8 * i);
        }
        return buf;
    }

    /**
     * 把byte 转为 字符串的bit
     * 3 ---> 0000 0011
     */
    public static String byteToBit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }

    /**
     * 字节数组转换为int数值
     * 十六进制的开头一般都以0x开头
     *
     * @param bytes
     * @return
     */
    public static int byteToInt(byte[] bytes) {
        if (bytes.length != 4) {
            return 0;
        }
        return (bytes[0]) & 0xff | (bytes[1] << 8) & 0xff00 | (bytes[2] << 16) & 0xff0000 | (bytes[3] << 24) & 0xff000000;
    }

    /**
     * 字节数组转换为short数值
     * 十六进制的开头一般都以0x开头
     *
     * @param bytes
     * @return
     */
    public static short byteToShort(byte[] bytes) {
        if (bytes.length != 2) {
            return 0;
        }
        return (short) ((bytes[0]) & 0xff
                | (bytes[1] << 8) & 0xff00);
    }

    /**
     * 字节数组转换为long数值
     * 十六进制的开头一般都以0x开头
     *
     * @param bytes
     * @return
     */
    public static long byteToLong(byte[] bytes) {
        if (bytes.length != 8) {
            return 0;
        }
        return ((long) bytes[0]) & 0xff
                | (((long) bytes[1]) << 8 & 0xff00)
                | (((long) bytes[2]) << 16 & 0xff0000)
                | (((long) bytes[3]) << 24 & 0xff000000)
                | (((long) bytes[4]) << 32 & 0xff00000000L)
                | (((long) bytes[5]) << 40 & 0xff0000000000L)
                | (((long) bytes[6]) << 48 & 0xff000000000000L)
                | (((long) bytes[7]) << 56 & 0xff00000000000000L);
    }

    /**
     * 字节数组转换为String（只支持传输英文解析）
     *
     * @param bytes
     * @return
     */
    public static String byteToString(byte[] bytes) {
        if (bytes.length == 0) {
            return null;
        }
        int len = bytes.length;
        char[] encodeChar = new char[len];
        for (int i = 0; i < bytes.length; i++) {
            encodeChar[i] = (char) bytes[i];
        }
        return String.valueOf(encodeChar);
    }


    /**
     * String 转换为 字节数组
     * 目前只支持英文解析
     *
     * @param str
     * @return
     */
    public static byte[] stringToBytes(String str) {
        if (str == null || str.length() == 0) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length()];
        char[] chars = str.toCharArray();
        for (int i = 0; i < bytes.length; i++) {
            int k = chars[i];
            bytes[i] = (byte) k;
        }
        return bytes;
    }

    /**
     * 将链表序列化为字符串存入json文件中
     *
     * @param objList
     * @return
     * @throws IOException
     */
    public static String convertForList(Object objList) {
        return JSON.toJSONString(objList, true);
    }

    /**
     * 将json文件中的内容读取出来，反序列化为链表
     *
     * @param listString
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<T> convertForListFromFile(String listString, Class<T> clazz) {
        return JSON.parseArray(listString, clazz);
    }
}
