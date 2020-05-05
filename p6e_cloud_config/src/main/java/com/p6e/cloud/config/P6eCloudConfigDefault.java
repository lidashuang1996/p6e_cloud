package com.p6e.cloud.config;

import com.p6e.cloud.config.pattern.P6eCloudConfigGroupPatternDefault;
import com.p6e.cloud.config.service.P6eCloudConfigServiceDefault;
import com.p6e.cloud.config.service.P6eCloudConfigWebSocketServiceDefault;

public class P6eCloudConfigDefault extends P6eCloudConfig {

    public P6eCloudConfigDefault() {
        this.setPattern("GROUP");
        this.setService(new String[] { "WEBSOCKET" });
        this.setBaseService(new P6eCloudConfigServiceDefault());
        this.setWebSocketService(new P6eCloudConfigWebSocketServiceDefault());
        this.setGroupPattern(new P6eCloudConfigGroupPatternDefault());
    }
}
