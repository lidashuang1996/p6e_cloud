package com.p6e.cloud.application;

import com.p6e.cloud.config.P6eCloudConfig;
import com.p6e.cloud.config.P6eCloudConfigDefault;
import com.p6e.cloud.config.service.P6eCloudConfigService;
import com.p6e.cloud.config.service.P6eCloudConfigSocketService;
import com.p6e.cloud.config.service.P6eCloudConfigWebSocketService;
import com.p6e.cloud.core.P6eCloudCore;
import com.p6e.cloud.core.P6eCloudCoreAuth;
import com.p6e.cloud.netty.*;
import com.p6e.cloud.netty.socket.P6eCloudNettyServiceSocket;
import com.p6e.cloud.netty.websocket.P6eCloudNettyServiceWebSocket;
import com.p6e.cloud.netty.websocket.P6eCloudNettyServiceWebSocketProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class P6eCloudApplication {

    private static final Logger logger = LoggerFactory.getLogger(P6eCloudApplication.class);

    private P6eCloudCore p6eCloudCore;
    private P6eCloudConfig p6eCloudConfig;
    private P6eCloudNettyAbstract[] p6eCloudNettyAbstracts;

    public P6eCloudApplication(P6eCloudCore p6eCloudCore, P6eCloudConfig p6eCloudConfig, P6eCloudCoreAuth p6eCloudCoreAuth) {
        this.p6eCloudCore = p6eCloudCore;
        this.p6eCloudConfig = p6eCloudConfig;
        String pattern = p6eCloudConfig.getPattern();
        switch (pattern.toUpperCase()) {
            case "TEST":
                break;
            case "GROUP":
                break;
            default:
                throw new RuntimeException("No corresponding message mode processor found");
        }
    }

    public P6eCloudApplication run() {
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
        p6eCloudNettyAbstracts = new P6eCloudNettyAbstract[programs.length];
        for (int i = 0; i < programs.length; i++) {
            switch (programs[i].toUpperCase()) {
                case "SOCKET":
                    P6eCloudConfigSocketService socketServiceConfig = p6eCloudConfig.getSocketService();
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
                                    p6eCloudCore.onOpen(client, map);
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
                                public void onMessageText(P6eCloudNettyClient client, String message) {
                                    p6eCloudCore.onMessageText(client, message);
                                }

                                @Override
                                public void onMessageBinary(P6eCloudNettyClient client, byte[] message) {
                                    p6eCloudCore.onMessageBinary(client, message);
                                }

                                @Override
                                public void onMessagePong(P6eCloudNettyClient client, byte[] message) {
                                    p6eCloudCore.onMessagePong(client, message);
                                }

                                @Override
                                public void onMessagePing(P6eCloudNettyClient client, byte[] message) {
                                    p6eCloudCore.onMessagePing(client, message);
                                }

                                @Override
                                public void onMessageContinuation(P6eCloudNettyClient client, byte[] message) {
                                    p6eCloudCore.onMessageContinuation(client, message);
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
        return this;
    }

    public static P6eCloudApplication create() {
        return new P6eCloudApplication(new P6eCloudConfigDefault());
    }

}
