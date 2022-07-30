package com.kuney.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author kuneychen
 * @since 2022/7/30 16:43
 */
@AllArgsConstructor
@Getter
public enum LoadBalancerCode {

    RANDOM(0),
    ROTATE(1);

    private final int code;

}
