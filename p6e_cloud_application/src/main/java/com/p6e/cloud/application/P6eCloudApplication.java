package com.p6e.cloud.application;

import com.p6e.cloud.config.P6eCloudConfig;
import com.p6e.cloud.config.P6eCloudConfigDefault;
import com.p6e.cloud.config.service.P6eCloudConfigService;
import com.p6e.cloud.config.service.P6eCloudConfigWebSocketService;
import com.p6e.cloud.core.P6eCloudCore;
import com.p6e.cloud.core.P6eCloudCoreAuth;
import com.p6e.cloud.core.P6eCloudCoreReactor;
import com.p6e.cloud.core.group.P6eCloudCoreGroup;
import com.p6e.cloud.core.group.P6eCloudCoreGroupReactor;
import com.p6e.cloud.netty.*;
import com.p6e.cloud.netty.socket.P6eCloudNettyServiceSocket;
import com.p6e.cloud.netty.websocket.P6eCloudNettyServiceWebSocket;
import com.p6e.cloud.netty.websocket.P6eCloudNettyServiceWebSocketProcessor;

import java.util.Map;

public class P6eCloudApplication {

    private P6eCloudCore p6eCloudCore;
    private P6eCloudConfig p6eCloudConfig;

    public P6eCloudApplication(P6eCloudConfig p6eCloudConfig,
                               P6eCloudCoreAuth p6eCloudCoreAuth,
                               P6eCloudCoreReactor p6eCloudCoreReactor) {
        this.p6eCloudConfig = p6eCloudConfig;
        String pattern = p6eCloudConfig.getPattern();
        switch (pattern.toUpperCase()) {
            case "TEST":
                break;
            case "GROUP":
                if (p6eCloudCoreReactor instanceof P6eCloudCoreGroupReactor) {
                    p6eCloudCore = new P6eCloudCoreGroup(p6eCloudCoreAuth, (P6eCloudCoreGroupReactor) p6eCloudCoreReactor);
                } else throw new RuntimeException("P6eCloudCoreGroupReactor conversion type exception");
                break;
            default:
                throw new RuntimeException("No corresponding message mode processor found");
        }
    }

    public void run() {
        /*
        * 编码解码的配置文件读取和赋值操作
        * 并且注入的 P6eCloudNettyAbstract 中使用
        */
        P6eCloudConfigService baseService = p6eCloudConfig.getBaseService();
        P6eCloudNettyEncoderDefault.Config[] encoderConfigs =
                new P6eCloudNettyEncoderDefault.Config[baseService.getEncoder().length];
        for (int i = 0; i < baseService.getEncoder().length; i++) {
            P6eCloudConfigService.Data data = baseService.getEncoder()[i];
            encoderConfigs[i] = new P6eCloudNettyEncoderDefault.Config(data.getLen(), data.getMode(), data.getType());
        }
        P6eCloudNettyDecoderDefault.Config[] decoderConfigs =
                new P6eCloudNettyDecoderDefault.Config[baseService.getDecoder().length];
        for (int i = 0; i < baseService.getDecoder().length; i++) {
            P6eCloudConfigService.Data data = baseService.getDecoder()[i];
            decoderConfigs[i] = new P6eCloudNettyDecoderDefault.Config(data.getLen(), data.getMode(), data.getType());
        }
        P6eCloudNettyAbstract.init(
                new P6eCloudNettyEncoderDefault(encoderConfigs),
                new P6eCloudNettyDecoderDefault(decoderConfigs)
        );
        /*
         * 初始化模块的执行对象
         */
        String[] programs = p6eCloudConfig.getService();
        P6eCloudNettyAbstract [] p6eCloudNettyAbstracts = new P6eCloudNettyAbstract[programs.length];
        for (int i = 0; i < programs.length; i++) {
            switch (programs[i].toUpperCase()) {
                case "SOCKET":
                    // P6eCloudConfigSocketService socketServiceConfig = p6eCloudConfig.getSocketService();
                    p6eCloudNettyAbstracts[i] = new P6eCloudNettyServiceSocket();
                    break;
                case "WEBSOCKET":
                    P6eCloudConfigWebSocketService webSocketServiceConfig = p6eCloudConfig.getWebSocketService();
                    p6eCloudNettyAbstracts[i] = new P6eCloudNettyServiceWebSocket(
                            webSocketServiceConfig.getPort(),
                            webSocketServiceConfig.getContentPath(),
                            new P6eCloudNettyServiceWebSocketProcessor() {
                                @Override
                                public void onOpen(P6eCloudNettyClient client, Map<String, String> map) {
                                    p6eCloudCore.onOpen(client, map, null);
                                }

                                @Override
                                public void onClose(P6eCloudNettyClient client) {
                                    p6eCloudCore.onClose(client);
                                }

                                @Override
                                public void onError(P6eCloudNettyClient client, Throwable throwable) {
                                    p6eCloudCore.onError(client, throwable);
                                }

                                @Override
                                public void onMessageBinary(P6eCloudNettyClient client, byte[] message) {
                                    p6eCloudCore.onMessage(client, new Object[] {message});
                                }

                                @Override
                                public void onMessageText(P6eCloudNettyClient client, String message) {
                                    p6eCloudCore.onMessage(client, new Object[] {message});
                                }
                            }
                    );
                    break;
                default:
                    throw new RuntimeException("No corresponding network service initiator found");
            }
        }
        // 启动指定的程序
        for (P6eCloudNettyAbstract netty : p6eCloudNettyAbstracts) netty.run();
    }

    public static P6eCloudApplication create(P6eCloudCoreReactor p6eCloudCoreReactor) {
        return create(new P6eCloudConfigDefault(), null, p6eCloudCoreReactor);
    }

    public static P6eCloudApplication create(P6eCloudConfig p6eCloudConfig, P6eCloudCoreReactor p6eCloudCoreReactor) {
        return create(p6eCloudConfig, null, p6eCloudCoreReactor);
    }

    public static P6eCloudApplication create(P6eCloudCoreAuth p6eCloudCoreAuth, P6eCloudCoreReactor p6eCloudCoreReactor) {
        return create(new P6eCloudConfigDefault(), p6eCloudCoreAuth, p6eCloudCoreReactor);
    }


    public static P6eCloudApplication create(P6eCloudConfig p6eCloudConfig,
                                             P6eCloudCoreAuth p6eCloudCoreAuth,
                                             P6eCloudCoreReactor p6eCloudCoreReactor) {
        return new P6eCloudApplication(p6eCloudConfig, p6eCloudCoreAuth, p6eCloudCoreReactor);
    }

}
