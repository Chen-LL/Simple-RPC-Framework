package com.kuney.rpc.transport.netty.client;

import com.kuney.rpc.codec.CommonDecoder;
import com.kuney.rpc.codec.CommonEncoder;
import com.kuney.rpc.entity.URL;
import com.kuney.rpc.enums.RpcError;
import com.kuney.rpc.exception.RpcException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author kuneychen
 * @since 2022/7/25 20:49
 */
@Slf4j
public class ChannelProvider {

    private static EventLoopGroup eventLoopGroup;
    private static Bootstrap bootstrap = initializeBootstrap();
    private static Channel channel;
    private static final int MAX_RETRY_COUNT = 5;

    public static Channel get(URL url) {
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast(new CommonEncoder())
                        .addLast(new CommonDecoder())
                        .addLast(new NettyClientHandler());
            }
        });
        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            connect(url, countDownLatch);
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("获取channel对象失败：{}", e.getMessage());
        }
        return channel;
    }

    private static void connect(URL url, CountDownLatch countDownLatch) {
        connect(bootstrap, url, 0, countDownLatch);
    }

    /*
        Netty客户端创建channel对象，实现失败重试机制
     */
    private static void connect(Bootstrap bootstrap, URL url, int retry, CountDownLatch countDownLatch) {
        bootstrap.connect(url.getHost(), url.getPort())
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        channel = future.channel();
                        countDownLatch.countDown();
                        log.info("客户端连接到服务器 {}:{}", url.getHost(), url.getPort());
                        return;
                    }
                    if (retry == MAX_RETRY_COUNT) {
                        log.error("客户端连接失败！重试次数已用完，放弃连接");
                        countDownLatch.countDown();
                        throw new RpcException(RpcError.FAILED_TO_CONNECT_SERVER);
                    }
                    log.error("连接失败，尝试第{}次重连", retry + 1);
                    // 重连延迟时间
                    long delay = 1 << retry;
                    // 利用schedule在给定延迟时间后重新连接
                    bootstrap.config().group().schedule(
                            () -> connect(bootstrap, url, retry + 1, countDownLatch),
                            delay,
                            TimeUnit.SECONDS
                    );
                });
    }

    private static Bootstrap initializeBootstrap() {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                //连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                //启用该功能时，TCP会主动探测空闲连接的有效性。可以将此功能视为TCP的心跳机制，默认的心跳间隔是7200s即2小时。
                .option(ChannelOption.SO_KEEPALIVE, true)
                //配置Channel参数，nodelay没有延迟，true就代表禁用Nagle算法，减小传输延迟。
                .option(ChannelOption.TCP_NODELAY, true);
        return bootstrap;
    }

}
