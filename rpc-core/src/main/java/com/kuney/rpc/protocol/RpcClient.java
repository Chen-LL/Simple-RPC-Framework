package com.kuney.rpc.protocol;

import com.kuney.rpc.entity.RpcRequest;
import com.kuney.rpc.entity.URL;

/**
 * @author kuneychen
 * @since 2022/7/13 15:49
 */
public interface RpcClient {

    Object send(RpcRequest rpcRequest, URL url);

}
