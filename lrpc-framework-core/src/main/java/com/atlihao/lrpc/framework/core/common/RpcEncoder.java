package com.atlihao.lrpc.framework.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Description: RPC编码器
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 9:33 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 9:33 上午
 * @Version: 1.0.0
 */
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcProtocol rpcProtocol, ByteBuf byteBuf) throws Exception {
        byteBuf.writeShort(rpcProtocol.getMagicNumber());
        byteBuf.writeInt(rpcProtocol.getContentLength());
        byteBuf.writeBytes(rpcProtocol.getContent());
    }

}
