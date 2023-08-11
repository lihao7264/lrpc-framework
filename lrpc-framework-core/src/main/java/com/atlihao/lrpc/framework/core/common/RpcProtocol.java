package com.atlihao.lrpc.framework.core.common;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

import static com.atlihao.lrpc.framework.core.common.constants.RpcConstants.MAGIC_NUMBER;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/8 9:34 上午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/8 9:34 上午
 * @Version: 1.0.0
 */
@Data
@ToString
public class RpcProtocol implements Serializable {
    private static final long serialVersionUID = 5730948517940175387L;

    /**
     * 魔数
     */
    private short magicNumber = MAGIC_NUMBER;

    /**
     * 内容长度（协议传输核心数据的长度）
     */
    private int contentLength;

    /**
     * 内容（数据包的字节数组）
     * RpcInvocation类的字节数组，在RpcInvocation中包含更多的调用信息（即RpcInvocation类）
     */
    private byte[] content;

    public RpcProtocol(byte[] content) {
        this.contentLength = content.length;
        this.content = content;
    }
}
