package com.atlihao.lrpc.framework.core.router;

import com.atlihao.lrpc.framework.core.common.ChannelFutureWrapper;
import lombok.Data;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/10 9:22 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/10 9:22 上午
 * @Version: 1.0.0
 */
@Data
public class Selector {

    /**
     * 服务命名
     * 举例: com.atlihao.test.DataService
     */
    private String providerServiceName;

    /**
     * 经过二次筛选后的future集合
     */
    private ChannelFutureWrapper[] channelFutureWrappers;

}
