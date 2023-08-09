package com.atlihao.lrpc.framework.core.common.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 公用缓存 存储提供者等公共信息
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 9:26 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 9:26 上午
 * @Version: 1.0.0
 */
public class CommonServerCache {

    /**
     * <服务接口,服务实例>：com.atlihao.lrpc.framework.interfaces.DataService -> DataServiceImpl@1770
     */
    public static final Map<String, Object> PROVIDER_CLASS_MAP = new HashMap<>();

}
