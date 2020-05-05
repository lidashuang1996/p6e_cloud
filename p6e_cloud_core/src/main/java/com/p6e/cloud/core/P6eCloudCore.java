package com.p6e.cloud.core;

import com.p6e.cloud.netty.P6eCloudNettyClient;

import java.util.Map;

public abstract class P6eCloudCore {

    public abstract void onOpen(P6eCloudNettyClient client, Map<String, String> map, Object message);

    public abstract void onClose(P6eCloudNettyClient client);

    public abstract void onError(P6eCloudNettyClient client, Throwable throwable);

    public abstract void onMessage(P6eCloudNettyClient client, Object[] message);

}
