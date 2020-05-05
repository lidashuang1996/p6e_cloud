package com.p6e.cloud.netty;

import java.util.Map;

public interface P6eCloudNettyServiceProcessor {

    public void onOpen(P6eCloudNettyClient client, Map<String, String> map);

    public void onClose(P6eCloudNettyClient client);

    public void onError(P6eCloudNettyClient client, Throwable throwable);

    public void onMessageText(P6eCloudNettyClient client, String message);

    public void onMessageBinary(P6eCloudNettyClient client, byte[] message);

    public void onMessagePong(P6eCloudNettyClient client, byte[] message);

    public void onMessagePing(P6eCloudNettyClient client, byte[] message);

    public void onMessageContinuation(P6eCloudNettyClient client, byte[] message);

}
