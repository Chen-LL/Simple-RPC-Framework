package com.kuney.rpc.transport.dto;

import com.kuney.rpc.enums.ResponseCode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author kuneychen
 * @since 2022/7/12 16:23
 */
@Data
@NoArgsConstructor
public class RpcResponse<T> implements Serializable {

    private String requestId;
    private Integer code;
    private String message;
    private T data;

    public static <T> RpcResponse<T> success(T data, String requestId) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setRequestId(requestId);
        response.setCode(ResponseCode.SUCCESS.getCode());
        response.setMessage(ResponseCode.SUCCESS.getMessage());
        response.setData(data);
        return response;
    }

    public static <T> RpcResponse<T> fail(ResponseCode code , String requestId) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setRequestId(requestId);
        response.setCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }

}
