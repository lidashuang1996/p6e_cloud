package com.p6e.cloud.config.service;

public class P6eCloudConfigWebSocketServiceDefault extends P6eCloudConfigWebSocketService {

    public P6eCloudConfigWebSocketServiceDefault() {
        this.setPort(7600);
        this.setContentPath("/");
        this.setMaxConnectLength(30000);
    }
}
