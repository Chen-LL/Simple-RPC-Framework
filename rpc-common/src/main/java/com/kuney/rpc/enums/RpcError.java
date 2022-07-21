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
    UNKNOWN_SERIALIZER("Unknown serializer."),
    FAILED_TO_CONNECT_SERVICE_REGISTRY("Failed to connect service registry."),
    FAILED_TO_REGISTER_SERVICE("Failed to register service."),
    FAILED_TO_LOOKUP_SERVICE("Failed to lookup service.");

    private final String message;

}
