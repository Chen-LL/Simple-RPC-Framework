package com.kuney.rpc.config;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author kuneychen
 * @since 2022/7/30 16:56
 */
@Slf4j
public class ServerConfiguration extends RpcConfiguration {

    public static String getHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getPort() {
        String value = properties.getProperty("rpc.port");
        if (value == null) {
            return 10000;
        }
        return Integer.parseInt(value);
    }

    public static String getServiceAddress() {
        String value = properties.getProperty("rpc.nacos.address");
        if (value == null) {
            return "127.0.0.1:8848";
        }
        return value;
    }

}
