package com.atlihao.lrpc.framework.core.server;

import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Description: 最大连接数限制处理器
 * @Author: lihao726726
 * @CreateDate: 2023/8/13 10:26 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/13 10:26 上午
 * @Version: 1.0.0
 */
@Slf4j
@ChannelHandler.Sharable
public class MaxConnectionLimitHandler extends ChannelInboundHandlerAdapter {

    /**
     * 最大连接数
     */
    private final int maxConnectionNum;

    /**
     * 连接数
     */
    private final AtomicInteger numConnection = new AtomicInteger(0);

    private final Set<Channel> childChannel = Collections.newSetFromMap(new ConcurrentHashMap<>());
    /**
     * 关闭掉的连接数
     * 在jdk1.8后，出现的对于AtomicLong的优化版本
     */
    private final LongAdder numDroppedConnections = new LongAdder();
    /**
     *
     */
    private final AtomicBoolean loggingScheduled = new AtomicBoolean(false);

    public MaxConnectionLimitHandler(int maxConnectionNum) {
        this.maxConnectionNum = maxConnectionNum;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = (Channel) msg;
        int conn = numConnection.incrementAndGet();
        if (conn > 0 && conn <= maxConnectionNum) {
            this.childChannel.add(channel);
            // 添加通道关闭监听
            channel.closeFuture().addListener(future -> {
                childChannel.remove(channel);
                numConnection.decrementAndGet();
            });
            super.channelRead(ctx, msg);
        } else {
            numConnection.decrementAndGet();
            // 避免产生大量的time_wait连接
            channel.config().setOption(ChannelOption.SO_LINGER, 0);
            channel.unsafe().closeForcibly();
            numDroppedConnections.increment();
            // 加cas判断，可减少一些并发请求的压力,定期地执行一些日志打印
            if (loggingScheduled.compareAndSet(false, true)) {
                ctx.executor().schedule(this::writeNumDroppedConnectionLog, 1, TimeUnit.SECONDS);
            }
        }
    }

    /**
     * 记录连接失败的日志
     */
    private void writeNumDroppedConnectionLog() {
        loggingScheduled.set(false);
        final long dropped = numDroppedConnections.sumThenReset();
        if (dropped > 0) {
            log.error("Dropped {} connection(s) to protect server,maxConnection is {}", dropped, maxConnectionNum);
        }
    }
}
