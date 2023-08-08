package com.atlihao.lrpc.framework.core.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @Description:
 * @Author: 仲阳-李豪
 * @CreateDate: 2023/8/5 6:34 下午
 * @UpdateUser: 仲阳-李豪
 * @UpdateDate: 2023/8/5 6:34 下午
 * @Version: 1.0.0
 */
public class BioServer {

    private static ThreadPoolExecutor BIO_SERVER_EXECUTORS = new ThreadPoolExecutor(10, 10, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "bio-server-executors");
        }
    });

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(1009));
        while (true) {
            // 阻塞状态点1
            Socket accept = serverSocket.accept();
            System.out.println("获取新连接");
            BIO_SERVER_EXECUTORS.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        InputStream inputStream = null;
                        try {
                            // 阻塞状态点2
                            inputStream = accept.getInputStream();
                            byte[] request = new byte[1024];
                            int len = inputStream.read(request);
                            if (len != 1) {
                                System.out.println("[response] " + new String(request, 0, len));
                                OutputStream outputStream = accept.getOutputStream();
                                outputStream.write("response data".getBytes());
                                outputStream.flush();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }
            });
        }

    }

}
