package com.p6e.cloud.netty.websocket;

import com.p6e.cloud.netty.P6eCloudNettyClient;
import com.p6e.cloud.netty.P6eCloudNettyServiceProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class P6eCloudNettyServiceWebSocketProcessor implements P6eCloudNettyServiceProcessor {

    /** 注入的日志对象 */
    private static final Logger logger = LoggerFactory.getLogger(P6eCloudNettyServiceWebSocketProcessor.class);

    @Override
    public void onOpen(P6eCloudNettyClient client, Map<String, String> map) {
        logger.debug("Web Socket onOpen ==> " + map);
    }

    @Override
    public void onClose(P6eCloudNettyClient client) {
        logger.debug("Web Socket onClose");
    }

    @Override
    public void onError(P6eCloudNettyClient client, Throwable throwable) {
        logger.debug("Web Socket onError");
    }

    @Override
    public void onMessageText(P6eCloudNettyClient client, String message) {
        logger.debug("Web Socket onMessageText");
    }

    @Override
    public void onMessageBinary(P6eCloudNettyClient client, byte[] message) {
        logger.debug("Web Socket onMessageBinary");
    }

    @Override
    public void onMessagePong(P6eCloudNettyClient client, byte[] message) {
        logger.debug("Web Socket onMessagePong");
    }

    @Override
    public void onMessagePing(P6eCloudNettyClient client, byte[] message) {
        logger.debug("Web Socket onMessagePing");
    }

    @Override
    public void onMessageContinuation(P6eCloudNettyClient client, byte[] message) {
        logger.debug("Web Socket onMessageContinuation");
    }

}
