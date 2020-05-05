package com.p6e.cloud.config.service;

public class P6eCloudConfigWebSocketService {

    /** 端口 */
    private int port;

    /** 最大的连接数 */
    private int maxConnectLength;

    private String contentPath;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxConnectLength() {
        return maxConnectLength;
    }

    public void setMaxConnectLength(int maxConnectLength) {
        this.maxConnectLength = maxConnectLength;
    }

    public String getContentPath() {
        return contentPath;
    }

    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }
}
