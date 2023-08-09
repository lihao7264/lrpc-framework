package com.atlihao.lrpc.framework.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static com.atlihao.lrpc.framework.core.common.constants.RpcConstants.MAGIC_NUMBER;

/**
 * @Description: RPC解码器
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 9:52 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 9:52 上午
 * @Version: 1.0.0
 */
public class RpcDecoder extends ByteToMessageDecoder {

    /**
     * 协议的开头部分的标准长度
     */
    public final int BASE_LENGTH = 2 + 4;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        if (byteBuf.readableBytes() >= BASE_LENGTH) {
            // 防止收到一些体积过大的数据包 目前限制在1000大小，后期版本这里是可配置模式
            if (byteBuf.readableBytes() > 1000) {
                byteBuf.skipBytes(byteBuf.readableBytes());
            }
            int beginReader;
            while (true) {
                beginReader = byteBuf.readerIndex();
                byteBuf.markReaderIndex();
                // 这里对应RpcProtocol的魔数
                if (byteBuf.readShort() == MAGIC_NUMBER) {
                    break;
                } else {
                    // 不是魔数开头，则说明是非法客户端发来的数据包
                    channelHandlerContext.close();
                    return;
                }
            }
            // 这里对应RpcProtocol对象的contentLength字段
            int length = byteBuf.readInt();
            // 说明剩余数据包不是完整的，这里需重置下读索引
            if (byteBuf.readableBytes() < length) {
                byteBuf.readerIndex(beginReader);
                return;
            }
            // 这里是RpcProtocol对象的content字段
            byte[] data = new byte[length];
            byteBuf.readBytes(data);
            RpcProtocol rpcProtocol = new RpcProtocol(data);
            out.add(rpcProtocol);
        }
    }
}
