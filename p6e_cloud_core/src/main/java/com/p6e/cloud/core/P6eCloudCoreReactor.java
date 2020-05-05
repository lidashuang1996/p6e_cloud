package com.p6e.cloud.core;

import com.p6e.cloud.netty.P6eCloudNettyClient;

public interface P6eCloudCoreReactor {

    public void onMessageClient(P6eCloudNettyClient client, Object[] message);

    public void onMessageOther(Object[] message);

}
