package com.kuney.rpc.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author kuneychen
 * @since 2022/7/12 16:26
 */
@AllArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS(200, "success"),
    FAIL(500, "fail"),
    METHOD_NOT_FOUND(500, "Method not found"),
    CLASS_NOT_FOUND(500, "Class not found");

    private final int code;
    private final String message;

}
