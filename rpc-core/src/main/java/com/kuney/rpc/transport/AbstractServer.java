package com.kuney.rpc.transport;

import com.kuney.rpc.annotation.RpcService;
import com.kuney.rpc.annotation.RpcServiceScan;
import com.kuney.rpc.config.ServerConfiguration;
import com.kuney.rpc.entity.URL;
import com.kuney.rpc.enums.RpcError;
import com.kuney.rpc.exception.RpcException;
import com.kuney.rpc.factory.SingletonFactory;
import com.kuney.rpc.registry.LocalServiceProvider;
import com.kuney.rpc.registry.NacosServiceRegistry;
import com.kuney.rpc.registry.ServiceRegistry;
import com.kuney.rpc.util.ReflectUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * @author kuneychen
 * @since 2022/7/20 22:25
 */
@Slf4j
public abstract class AbstractServer implements RpcServer {

    protected String host;
    protected int port;
    protected LocalServiceProvider serviceProvider;
    protected ServiceRegistry serviceRegistry;

    public AbstractServer() {
        this.host = ServerConfiguration.getHost();
        this.port = ServerConfiguration.getPort();
        this.serviceProvider = SingletonFactory.getInstance(LocalServiceProvider.class);
        this.serviceRegistry = new NacosServiceRegistry();
    }

    @Override
    public <T> void publishService(String serviceName, Object service) {
        serviceProvider.register(serviceName, service);
        serviceRegistry.register(serviceName, new URL(host, port));
    }

    public void scanService() {
        String startClassName = ReflectUtils.getStartClassName();
        Class<?> startClass = null;
        try {
            startClass = Class.forName(startClassName);
        } catch (ClassNotFoundException e) {
            log.error("找不到启动类");
            throw new RpcException(RpcError.UNKNOWN_ERROR);
        }
        RpcServiceScan rpcServiceScan = startClass.getAnnotation(RpcServiceScan.class);
        if (rpcServiceScan == null) {
            log.error("启动类缺少 @RpcServiceScan 注解");
            throw new RpcException(RpcError.SERVICE_SCAN_PACKAGE_NOT_FOUND);
        }
        String basePackage = rpcServiceScan.value();
        if ("".equals(basePackage)) {
            basePackage = startClassName.substring(0, startClassName.lastIndexOf("."));
        }
        Set<Class<?>> classes = ReflectUtils.getClasses(basePackage);
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(RpcService.class)) {
                String serviceName = clazz.getAnnotation(RpcService.class).value();
                Object service = null;
                try {
                    service = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("创建 [{}] 对象失败：{}", clazz.getCanonicalName(), e.getMessage());
                    continue;
                }
                if ("".equals(serviceName)) {
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> anInterface : interfaces) {
                        publishService(anInterface.getCanonicalName(), service);
                    }
                } else {
                    publishService(serviceName, service);
                }
            }
        }
    }
}
