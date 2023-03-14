package com.kuney.rpc.transport;

import com.kuney.rpc.transport.dto.RpcRequest;
import com.kuney.rpc.entity.URL;

/**
 * @author kuneychen
 * @since 2022/7/13 15:49
 */
public interface RpcClient {

    Object send(RpcRequest rpcRequest, URL url);

}
