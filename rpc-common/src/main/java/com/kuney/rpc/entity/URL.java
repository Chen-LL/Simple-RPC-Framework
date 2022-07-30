package com.kuney.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author kuneychen
 * @since 2022/7/12 17:02
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class URL {

    private String host;
    private int port;

}
