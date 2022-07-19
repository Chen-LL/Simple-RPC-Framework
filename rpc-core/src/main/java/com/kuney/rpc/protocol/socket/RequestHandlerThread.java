package com.kuney.rpc.protocol.socket;

import com.kuney.rpc.entity.RpcRequest;
import com.kuney.rpc.entity.RpcResponse;
import com.kuney.rpc.handler.RequestHandler;
import com.kuney.rpc.registry.ServiceRegistry;
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
    private ServiceRegistry serviceRegistry;

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceRegistry serviceRegistry) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        try (
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ) {
            RpcRequest rpcRequest = (RpcRequest) ois.readObject();
            Object service = serviceRegistry.getService(rpcRequest.getInterfaceName());
            Object result = requestHandler.handle(rpcRequest, service);
            oos.writeObject(RpcResponse.success(result));
            oos.flush();
        } catch (IOException | ClassNotFoundException e) {
            log.error("请求处理时发生错误：{}", e.getMessage());
        }
    }
}
