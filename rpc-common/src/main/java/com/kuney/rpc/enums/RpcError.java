package com.kuney.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author kuneychen
 * @since 2022/7/12 22:40
 */
@Getter
@AllArgsConstructor
public enum RpcError {

    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("Service not implement any interface."),
    SERVICE_NOT_FOUND("Service not found."),
    UNKNOWN_PROTOCOL("Unknown protocol."),
    UNKNOWN_PACKAGE_TYPE("Unknown package type."),
    UNKNOWN_SERIALIZER("Unknown serializer.");

    private final String message;

}
