package com.kuney.test.api;

import com.kuney.rpc.annotation.RpcService;
import com.kuney.rpc.api.HelloService;

/**
 * @author kuneychen
 * @since 2022/7/12 18:11
 */
@RpcService
public class HelloServiceImpl implements HelloService {
    @Override
    public Object hello(String content) {
        return "Hello: " + content;
    }
}
