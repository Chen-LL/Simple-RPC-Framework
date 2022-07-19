package com.kuney.rpc.protocol.netty.server;

import com.kuney.rpc.entity.RpcRequest;
import com.kuney.rpc.entity.RpcResponse;
import com.kuney.rpc.handler.RequestHandler;
import com.kuney.rpc.registry.DefaultServiceRegistry;
import com.kuney.rpc.registry.ServiceRegistry;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kuneychen
 * @since 2022/7/19 21:33
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static RequestHandler requestHandler;
    private static ServiceRegistry serviceRegistry;

    static {
        requestHandler = new RequestHandler();
        serviceRegistry = new DefaultServiceRegistry();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        log.info("服务器接收到请求：{}", request.toString());
        Object service = serviceRegistry.getService(request.getInterfaceName());
        Object result = requestHandler.handle(request, service);
        ctx.writeAndFlush(RpcResponse.success(result)).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("服务器处理接口调用时出错：{}", cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }
}
