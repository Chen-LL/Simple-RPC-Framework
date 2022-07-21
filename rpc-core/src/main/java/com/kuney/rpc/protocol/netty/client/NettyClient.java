package com.kuney.rpc.protocol.netty.client;

import com.kuney.rpc.codec.CommonDecoder;
import com.kuney.rpc.codec.CommonEncoder;
import com.kuney.rpc.entity.RpcRequest;
import com.kuney.rpc.entity.RpcResponse;
import com.kuney.rpc.protocol.RpcClient;
import com.kuney.rpc.protocol.URL;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuneychen
 * @since 2022/7/19 21:37
 */
@Slf4j
public class NettyClient implements RpcClient {

    private static final Bootstrap bootstrap;

    static {
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new CommonDecoder())
                                .addLast(new CommonEncoder())
                                .addLast(new NettyClientHandler());
                    }
                });
    }

    @Override
    public Object send(RpcRequest rpcRequest, URL url) {
        try {
            ChannelFuture channelFuture = bootstrap.connect(url.getHost(), url.getPort()).sync();
            log.info("客户端连接到服务器 {}:{}", url.getHost(), url.getPort());
            Channel channel = channelFuture.channel();
            if (channel != null) {
                channel.writeAndFlush(rpcRequest)
                        .addListener(future -> {
                            if (future.isSuccess()) {
                                log.info("客户端成功发送消息：{}", rpcRequest.toString());
                            } else {
                                log.info("客户端发送消息失败：{}", future.cause().getMessage());
                            }
                        });
                channel.closeFuture().sync();
                // 阻塞获取返回结果
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse rpcResponse = channel.attr(key).get();
                return rpcResponse;
            }
        } catch (InterruptedException e) {
            log.info("客户端发送消息失败：{}", e.getMessage());
        }
        return null;
    }
}
