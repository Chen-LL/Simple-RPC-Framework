package com.kuney.rpc.entity;

import com.kuney.rpc.enums.ResponseCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kuneychen
 * @since 2022/7/12 16:23
 */
@Data
public class RpcResponse<T> implements Serializable {

    private Integer code;
    private String message;
    private T data;

    public static <T> RpcResponse<T> success(T data) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(ResponseCode.SUCCESS.getCode());
        response.setMessage(ResponseCode.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }

    public static <T> RpcResponse<T> fail(ResponseCode code) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }

}
