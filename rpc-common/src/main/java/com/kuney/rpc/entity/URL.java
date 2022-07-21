package com.kuney.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author kuneychen
 * @since 2022/7/12 17:02
 */
@Data
@AllArgsConstructor
public class URL implements Serializable {

    private String host;
    private int port;

}
