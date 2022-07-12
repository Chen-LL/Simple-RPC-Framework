package com.kuney.rpc.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kuneychen
 * @since 2022/7/12 16:21
 */
@Builder
@Data
public class RpcRequest implements Serializable {

    private String interfaceName;
    private String methodName;
    private Class<?>[] paramTypes;
    private Object[] params;

}
