package com.atlihao.lrpc.framework.core.common.constants;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 9:30 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 9:30 上午
 * @Version: 1.0.0
 */
public class RpcConstants {

    /**
     * 魔数
     */
    public static final short MAGIC_NUMBER = 19812;

    public static final String JDK_PROXY_TYPE = "jdk";

    public static final String JAVASSIST_PROXY_TYPE = "javassist";

    public static final String RANDOM_ROUTER_TYPE = "random";

    public static final String NEW_RANDOM_ROUTER_TYPE = "newRandom";

    public static final String ROTATE_ROUTER_TYPE = "rotate";

    public static final String JDK_SERIALIZE_TYPE = "jdk";

    public static final String FAST_JSON_SERIALIZE_TYPE = "fastJson";

    public static final String HESSIAN2_SERIALIZE_TYPE = "hessian2";

    public static final String KRYO_SERIALIZE_TYPE = "kryo";

    /**
     * 默认请求超时时间
     */
    public static final Long DEFAULT_TIMEOUT = 3000L;

    /**
     * 默认线程数
     */
    public static final Integer DEFAULT_THREAD_NUMS = 256;

    /**
     * 默认队列大小
     */
    public static final Integer DEFAULT_QUEUE_SIZE = 512;

}
