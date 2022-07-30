package com.kuney.rpc.transport.netty.client;

import com.kuney.rpc.transport.netty.codec.CommonDecoder;
import com.kuney.rpc.transport.netty.codec.CommonEncoder;
import com.kuney.rpc.entity.URL;
import com.kuney.rpc.enums.RpcError;
import com.kuney.rpc.exception.RpcException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author kuneychen
 * @since 2022/7/25 20:49
 */
@Slf4j
public class ChannelProvider {

    private static EventLoopGroup eventLoopGroup;
    private static Bootstrap bootstrap = initializeBootstrap();
    private static Map<URL, Channel> channels = new ConcurrentHashMap<>();
    private static final int MAX_RETRY_COUNT = 5;

    public static Channel get(URL url) {
        Channel channel = channels.get(url);
        if (channel != null && channel.isActive()) {
            return channel;
        } else {
            channels.remove(url);
        }
        channel = connect(url);
        channels.put(url, channel);
        return channel;
    }

    private static Channel connect(URL url) {
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast(new CommonEncoder())
                        .addLast(new CommonDecoder())
                        .addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS))
                        .addLast(new NettyClientHandler());
            }
        });
        try {
            return connect(bootstrap, url, 0);
        } catch (ExecutionException | InterruptedException e) {
            log.error("客户端连接失败：{}", e.getMessage());
            return null;
        }
    }

    /*
        Netty客户端创建channel对象，实现失败重试机制
     */
    private static Channel connect(Bootstrap bootstrap, URL url, int retry) throws InterruptedException, ExecutionException {
        CompletableFuture<Channel> result = new CompletableFuture<>();
        bootstrap.connect(url.getHost(), url.getPort())
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        result.complete(future.channel());
                        log.info("客户端连接到服务器 {}:{}", url.getHost(), url.getPort());
                        return;
                    }
                    if (retry == MAX_RETRY_COUNT) {
                        log.error("客户端连接失败！重试次数已用完，放弃连接");
                        throw new RpcException(RpcError.FAILED_TO_CONNECT_SERVER);
                    }
                    log.error("连接失败，尝试第{}次重连", retry + 1);
                    // 重连延迟时间
                    long delay = 1 << retry;
                    // 利用schedule在给定延迟时间后重新连接
                    bootstrap.config().group().schedule(
                            () -> connect(bootstrap, url, retry + 1),
                            delay,
                            TimeUnit.SECONDS
                    );
                });
        return result.get();
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
