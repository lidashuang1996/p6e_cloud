package com.p6e.cloud.netty.websocket;

import com.p6e.cloud.netty.P6eCloudNettyServiceProcessor;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class P6eCloudNettyServiceWebSocketProcessor implements P6eCloudNettyServiceProcessor {

    /** 注入的日志对象 */
    private static final Logger logger = LoggerFactory.getLogger(P6eCloudNettyServiceWebSocketProcessor.class);

    @Override
    public void onOpen(ChannelHandlerContext context, Map<String, String> map) {
        logger.debug("Web Socket onOpen ==> " + map);
    }

    @Override
    public void onClose() {
        logger.debug("Web Socket onClose");
    }

    @Override
    public void onError(Throwable throwable) {
        logger.debug("Web Socket onError");
    }

    @Override
    public void onMessageText(String message) {
        logger.debug("Web Socket onMessageText");
    }

    @Override
    public void onMessageBinary(byte[] message) {
        logger.debug("Web Socket onMessageBinary");
    }

    @Override
    public void onMessagePong(byte[] message) {
        logger.debug("Web Socket onMessagePong");
    }

    @Override
    public void onMessagePing(byte[] message) {
        logger.debug("Web Socket onMessagePing");
    }

    @Override
    public void onMessageContinuation(byte[] message) {
        logger.debug("Web Socket onMessageContinuation");
    }
}
