package com.kuney.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author kuneychen
 * @since 2022/7/19 18:19
 */
@AllArgsConstructor
@Getter
public enum PackageType {

    REQUEST_PACK(0), RESPONSE_PACK(1);

    private final int type;
}
