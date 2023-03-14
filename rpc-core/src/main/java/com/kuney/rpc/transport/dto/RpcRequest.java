package com.kuney.rpc.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author kuneychen
 * @since 2022/7/12 16:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {

    private String requestId;
    private boolean heartBeat;
    private String interfaceName;
    private String methodName;
    private Class<?>[] paramTypes;
    private Object[] params;

}
