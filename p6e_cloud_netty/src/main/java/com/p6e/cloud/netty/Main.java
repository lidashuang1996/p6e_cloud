package com.p6e.cloud.netty;

import com.p6e.cloud.netty.websocket.P6eCloudNettyServiceWebSocket;

public class Main {
    public static void main(String[] args) {
        new P6eCloudNettyServiceWebSocket(12000).run();
    }
}
