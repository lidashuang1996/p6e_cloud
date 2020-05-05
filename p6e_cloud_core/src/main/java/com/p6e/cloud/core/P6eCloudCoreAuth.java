package com.p6e.cloud.core;

import com.p6e.cloud.netty.P6eCloudNettyClient;

import java.util.Map;

public interface P6eCloudCoreAuth {

    public boolean execute(P6eCloudNettyClient client, Map<String, String> map, Object message);

}
