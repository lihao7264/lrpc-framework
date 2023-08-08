package com.atlihao.lrpc.framework.core.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/6 8:44 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/6 8:44 下午
 * @Version: 1.0.0
 */
public class NioSocketClient {

    public static void main(String[] args) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8888));
            socketChannel.configureBlocking(Boolean.FALSE);
            while (true) {
                socketChannel.write(ByteBuffer.wrap(("this is test " + Thread.currentThread().getName()).getBytes()));
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
