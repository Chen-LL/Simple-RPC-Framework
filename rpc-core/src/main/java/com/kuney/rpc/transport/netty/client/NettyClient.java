package com.kuney.rpc.transport.netty.client;

import com.kuney.rpc.entity.RpcRequest;
import com.kuney.rpc.entity.RpcResponse;
import com.kuney.rpc.entity.URL;
import com.kuney.rpc.enums.ResponseCode;
import com.kuney.rpc.factory.SingletonFactory;
import com.kuney.rpc.transport.RpcClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * @author kuneychen
 * @since 2022/7/19 21:37
 */
@Slf4j
public class NettyClient implements RpcClient {

    private UnprocessedRequests unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);

    @Override
    public CompletableFuture<RpcResponse> send(RpcRequest rpcRequest, URL url) {
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
        Channel channel = ChannelProvider.get(url);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(rpcRequest)
                    .addListener((ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            log.info("客户端成功发送消息：{}", rpcRequest.toString());
                        } else {
                            log.error("客户端发送消息失败：{}", future.cause().getMessage());
                            future.channel().close();
                            unprocessedRequests.complete(RpcResponse.fail(ResponseCode.FAIL, rpcRequest.getRequestId()));
                        }
                    });
        } else {
            throw new IllegalStateException();
        }
        return resultFuture;
    }
}
