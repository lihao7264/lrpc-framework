package com.atlihao.lrpc.framework.core.common;

import io.netty.channel.ChannelFuture;
import lombok.Data;
import lombok.ToString;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/9 3:41 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/9 3:41 下午
 * @Version: 1.0.0
 */
@Data
@ToString
public class ChannelFutureWrapper {

    private ChannelFuture channelFuture;

    private String host;

    private Integer port;
}
