package com.kuney.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author kuneychen
 * @since 2022/7/19 18:11
 */
@AllArgsConstructor
@Getter
public enum SerializerCode {

    JSON(1);
    private final int code;
}
