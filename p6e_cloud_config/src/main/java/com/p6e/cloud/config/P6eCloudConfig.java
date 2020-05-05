package com.p6e.cloud.config;


import com.p6e.cloud.config.pattern.P6eCloudConfigGroupPattern;
import com.p6e.cloud.config.service.P6eCloudConfigService;
import com.p6e.cloud.config.service.P6eCloudConfigSocketService;
import com.p6e.cloud.config.service.P6eCloudConfigWebSocketService;

/**
 * P6eCloudCoreConfig
 * 核心配置文件
 * 不管采用什么方式读取配置信息
 * 最终的必须实现的配置类
 *
 * 1. 读取 Spring YML 文件读取配置信息数据
 * 2. 读取 XXX 配置信息
 * 3. 编程式读取配置信息
 */
public class P6eCloudConfig {

    /** 启动的服务 */
    private String[] service;

    private P6eCloudConfigService baseService;

    /** Socket 服务 */
    private P6eCloudConfigSocketService socketService;

    /** Web Socket 服务 */
    private P6eCloudConfigWebSocketService webSocketService;

    private String pattern;

    /** 核心模块的模式选择 */
    private P6eCloudConfigGroupPattern groupPattern;

    public String[] getService() {
        return service;
    }

    public void setService(String[] service) {
        this.service = service;
    }

    public P6eCloudConfigSocketService getSocketService() {
        return socketService;
    }

    public void setSocketService(P6eCloudConfigSocketService socketService) {
        this.socketService = socketService;
    }

    public P6eCloudConfigWebSocketService getWebSocketService() {
        return webSocketService;
    }

    public void setWebSocketService(P6eCloudConfigWebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    public P6eCloudConfigGroupPattern getGroupPattern() {
        return groupPattern;
    }

    public void setGroupPattern(P6eCloudConfigGroupPattern groupPattern) {
        this.groupPattern = groupPattern;
    }

    public P6eCloudConfigService getBaseService() {
        return baseService;
    }

    public void setBaseService(P6eCloudConfigService baseService) {
        this.baseService = baseService;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
