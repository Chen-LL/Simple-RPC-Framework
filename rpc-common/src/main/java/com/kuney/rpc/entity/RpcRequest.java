package com.kuney.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author kuneychen
 * @since 2022/7/12 16:21
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {

    private String interfaceName;
    private String methodName;
    private Class<?>[] paramTypes;
    private Object[] params;

}
