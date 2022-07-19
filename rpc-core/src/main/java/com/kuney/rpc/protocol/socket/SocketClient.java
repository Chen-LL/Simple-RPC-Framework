package com.kuney.rpc.protocol.socket;

import com.kuney.rpc.entity.RpcRequest;
import com.kuney.rpc.protocol.RpcClient;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author kuneychen
 * @since 2022/7/12 17:01
 */
@Slf4j
public class SocketClient implements RpcClient {

    @Override
    public Object send(RpcRequest rpcRequest) {
        try (
                Socket socket = new Socket("localhost", 8080);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        ) {
            oos.writeObject(rpcRequest);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("[SocketClient.send] 失败：{}", e.getMessage());
            return null;
        }
    }

}
