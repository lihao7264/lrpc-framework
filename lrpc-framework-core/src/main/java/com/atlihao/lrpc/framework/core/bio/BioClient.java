package com.atlihao.lrpc.framework.core.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * @Description:
 * @Author: lihao726726
 * @CreateDate: 2023/8/5 6:23 下午
 * @UpdateUser: lihao726726
 * @UpdateDate: 2023/8/5 6:23 下午
 * @Version: 1.0.0
 */
public class BioClient {

    private static ThreadPoolExecutor BIO_CLIENT_EXECUTORS = new ThreadPoolExecutor(1, 1, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"bio-client-executors");
        }
    });

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(1009));
        OutputStream outputStream = null;
        while (true) {
            // 输入需求发送的信息
            Scanner scanner = new Scanner(System.in);
            String nextLine = scanner.nextLine();
            outputStream = socket.getOutputStream();
            outputStream.write(nextLine.getBytes());
            outputStream.flush();
            System.out.println("发送结束");
            BIO_CLIENT_EXECUTORS.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            InputStream inputStream = socket.getInputStream();
                            byte[] response = new byte[1024];
                            int len = inputStream.read(response);
                            if (len != 1) {
                                System.out.println("获取到数据：" + new String(response, 0, len));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

    }


}
