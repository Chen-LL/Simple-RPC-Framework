package com.kuney.rpc.protocol.socket;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author kuneychen
 * @since 2022/7/12 17:10
 */
@Slf4j
public class SocketServer {

    private final ExecutorService threadPool;

    public SocketServer() {
        int corePoolSize = 5;
        int maxPoolSize = 50;
        long keepAliveTime = 60;
        threadPool = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );
    }

    public void register(Object service, int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("服务正在启动...");
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("客户端连接成功！IP = {}", socket.getInetAddress());
                threadPool.execute(new WorkerThead(socket, service));
            }
        } catch (IOException e) {
            log.error("连接时发生错误：{}", e.getMessage());
        }
    }

}
