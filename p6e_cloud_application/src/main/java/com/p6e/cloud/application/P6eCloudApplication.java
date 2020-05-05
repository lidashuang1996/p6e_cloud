package com.p6e.cloud.application;

import com.p6e.cloud.config.P6eCloudConfig;
import com.p6e.cloud.config.P6eCloudConfigDefault;
import com.p6e.cloud.config.service.P6eCloudConfigService;
import com.p6e.cloud.config.service.P6eCloudConfigSocketService;
import com.p6e.cloud.config.service.P6eCloudConfigWebSocketService;
import com.p6e.cloud.netty.*;
import com.p6e.cloud.netty.socket.P6eCloudNettyServiceSocket;
import com.p6e.cloud.netty.websocket.P6eCloudNettyServiceWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class P6eCloudApplication {

    private static final Logger logger = LoggerFactory.getLogger(P6eCloudApplication.class);

    private P6eCloudConfig p6eCloudConfig;
    private P6eCloudNettyAbstract[] p6eCloudNettyAbstracts;

    public P6eCloudApplication(P6eCloudConfig p6eCloudConfig) {
        this.p6eCloudConfig = p6eCloudConfig;
    }

    public P6eCloudApplication run() {
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
                            webSocketServiceConfig.getContentPath()
                    );
                    break;
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
