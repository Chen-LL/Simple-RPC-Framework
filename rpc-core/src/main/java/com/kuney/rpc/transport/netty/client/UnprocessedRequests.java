package com.kuney.rpc.transport.netty.client;

import com.kuney.rpc.transport.dto.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kuneychen
 * @since 2022/7/27 18:12
 */
@Slf4j
public class UnprocessedRequests {

    private static final Map<String, CompletableFuture<RpcResponse>> requests = new ConcurrentHashMap<>();

    public void put(String requestId, CompletableFuture<RpcResponse> future) {
        requests.put(requestId, future);
    }

    public void complete(RpcResponse response) {
        CompletableFuture<RpcResponse> future = requests.remove(response.getRequestId());
        if (future != null) {
            future.complete(response);
        } else {
            throw new IllegalStateException();
        }
    }

}
