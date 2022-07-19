package com.kuney.rpc.exception;

import com.kuney.rpc.enums.RpcError;

/**
 * @author kuneychen
 * @since 2022/7/12 22:41
 */
public class RpcException extends RuntimeException {

    public RpcException(RpcError error) {
        super(error.getMessage());
    }
}
