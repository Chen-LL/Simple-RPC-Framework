package com.kuney.rpc.protocol.socket;

import com.kuney.rpc.handler.RequestHandler;
import com.kuney.rpc.protocol.AbstractServer;
import com.kuney.rpc.registry.LocalServiceProvider;
import com.kuney.rpc.registry.NacosServiceRegistry;
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
public class SocketServer extends AbstractServer {

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 50;
    private static final long KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;

    private final ExecutorService threadPool;
    private final RequestHandler requestHandler;

    public SocketServer(String host, int port) {
        this.host = host;
        this.port = port;
        serviceRegistry = new NacosServiceRegistry();
        serviceProvider = new LocalServiceProvider();
        requestHandler = new RequestHandler();
        threadPool = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );
    }

    @Override
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            log.info("服务器启动......");
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("消费者连接 {}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket, requestHandler, serviceProvider));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("服务器启动发生错误：{}", e.getMessage());
        }
    }

}

