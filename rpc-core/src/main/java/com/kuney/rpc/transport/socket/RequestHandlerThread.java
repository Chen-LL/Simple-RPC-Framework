package com.kuney.rpc.transport.socket;

import com.kuney.rpc.entity.RpcRequest;
import com.kuney.rpc.entity.RpcResponse;
import com.kuney.rpc.handler.RequestHandler;
import com.kuney.rpc.registry.LocalServiceProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author kuneychen
 * @since 2022/7/12 17:35
 */
@Slf4j
public class RequestHandlerThread implements Runnable {

    private Socket socket;
    private RequestHandler requestHandler;
    private LocalServiceProvider serviceProvider;

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, LocalServiceProvider serviceProvider) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void run() {
        try (
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ) {
            RpcRequest rpcRequest = (RpcRequest) ois.readObject();
            Object service = serviceProvider.getService(rpcRequest.getInterfaceName());
            Object result = requestHandler.handle(rpcRequest, service);
            oos.writeObject(RpcResponse.success(result, rpcRequest.getRequestId()));
            oos.flush();
        } catch (IOException | ClassNotFoundException e) {
            log.error("请求处理时发生错误：{}", e.getMessage());
        }
    }
}
