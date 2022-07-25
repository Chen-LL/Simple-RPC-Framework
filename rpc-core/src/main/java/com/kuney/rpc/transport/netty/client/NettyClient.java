package com.kuney.rpc.transport.netty.client;

import com.kuney.rpc.entity.RpcRequest;
import com.kuney.rpc.entity.RpcResponse;
import com.kuney.rpc.entity.URL;
import com.kuney.rpc.transport.RpcClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuneychen
 * @since 2022/7/19 21:37
 */
@Slf4j
public class NettyClient implements RpcClient {

    @Override
    public Object send(RpcRequest rpcRequest, URL url) {
        try {
            Channel channel = ChannelProvider.get(url);
            if (channel != null) {
                channel.writeAndFlush(rpcRequest)
                        .addListener((ChannelFutureListener) future -> {
                            if (future.isSuccess()) {
                                log.info("客户端成功发送消息：{}", rpcRequest.toString());
                            } else {
                                log.info("客户端发送消息失败：{}", future.cause().getMessage());
                                future.channel().close();
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
