package com.atlihao.lrpc.framework.core.client;

import com.atlihao.lrpc.framework.core.common.RpcInvocation;
import com.atlihao.lrpc.framework.core.common.RpcProtocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.CLIENT_SERIALIZE_FACTORY;
import static com.atlihao.lrpc.framework.core.common.cache.CommonClientCache.RESP_MAP;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 1:20 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 1:20 下午
 * @Version: 1.0.0
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 客户端和服务端之间的数据都以RpcProtocol对象作为基本协议进行的交互
        RpcProtocol rpcProtocol = (RpcProtocol) msg;
        // 这里是传输参数更为详细的RpcInvocation对象字节数组。
        byte[] reqContent = rpcProtocol.getContent();
        RpcInvocation rpcInvocation = CLIENT_SERIALIZE_FACTORY.deserialize(reqContent, RpcInvocation.class);
        // 如果是单纯异步模式的话，响应Map集合中不会存在映射值
        Object async = rpcInvocation.getAttachments().get("async");
        if (async != null && Boolean.valueOf(String.valueOf(async))) {
            ReferenceCountUtil.release(msg);
            return;
        }

        // 通过之前发送的uuid来注入匹配的响应数值
        if (!RESP_MAP.containsKey(rpcInvocation.getUuid())) {
            throw new IllegalArgumentException("server response is error!");
        }
        // 将请求的响应结构放入一个Map集合中（集合key：uuid），该uuid在发送请求前已初始化好，所以只需起一个线程在后台遍历该map，查看对应的key是否有相应即可。
        // uuid放入map的时机：放入的操作被封装到代理类中进行实现
        RESP_MAP.put(rpcInvocation.getUuid(), rpcInvocation);
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }
}
