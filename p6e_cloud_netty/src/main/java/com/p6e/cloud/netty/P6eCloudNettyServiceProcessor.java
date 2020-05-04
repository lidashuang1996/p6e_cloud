package com.p6e.cloud.netty;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

public interface P6eCloudNettyServiceProcessor {

    public void onOpen(ChannelHandlerContext context, Map<String, String> map);

    public void onClose();

    public void onError(Throwable throwable);

    public void onMessageText(String message);

    public void onMessageBinary(byte[] message);

    public void onMessagePong(byte[] message);

    public void onMessagePing(byte[] message);

    public void onMessageContinuation(byte[] message);

}
