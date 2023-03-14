package com.kuney.rpc.transport.socket;

import com.kuney.rpc.transport.dto.RpcRequest;
import com.kuney.rpc.transport.dto.RpcResponse;
import com.kuney.rpc.handler.RequestHandler;
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

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler) {
        this.socket = socket;
        this.requestHandler = requestHandler;
    }

    @Override
    public void run() {
        try (
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ) {
            RpcRequest rpcRequest = (RpcRequest) ois.readObject();
            Object result = requestHandler.handle(rpcRequest);
            oos.writeObject(RpcResponse.success(result, rpcRequest.getRequestId()));
            oos.flush();
        } catch (IOException | ClassNotFoundException e) {
            log.error("请求处理时发生错误：{}", e.getMessage());
        }
    }
}
