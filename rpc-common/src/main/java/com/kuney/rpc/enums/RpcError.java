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

    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("Service not implement any interface"),
    SERVICE_NOT_FOUND("Service not found"),
    UNKNOWN_PROTOCOL("Unknown protocol"),
    UNKNOWN_PACKAGE_TYPE("Unknown package type"),
    UNKNOWN_SERIALIZER("Unknown serializer"),
    UNKNOWN_ERROR("Unknown error"),
    FAILED_TO_CONNECT_SERVICE_REGISTRY("Failed to connect service registry"),
    FAILED_TO_CONNECT_SERVER("Failed to connect server"),
    FAILED_TO_REGISTER_SERVICE("Failed to register service"),
    FAILED_TO_LOOKUP_SERVICE("Failed to lookup service"),
    SERVICE_SCAN_PACKAGE_NOT_FOUND("Service scan package not found"),
    SERVICE_INVOCATION_FAILURE("Service invocation failure"),
    REQUEST_NOT_MATCH_WITH_RESPONSE("Request not match with response"),
    NOT_SUPPORTED_SERIALIZE_ALGORITHM("Not supported serialize algorithm"),
    NOT_SUPPORTED_LOAD_BALANCE_ALGORITHM("Not supported load balance algorithm");

    private final String message;

}
