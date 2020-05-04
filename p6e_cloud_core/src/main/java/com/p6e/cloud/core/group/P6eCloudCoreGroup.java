package com.p6e.cloud.core.group;

import com.p6e.cloud.core.P6eCloudCore;
import com.p6e.cloud.core.P6eCloudCoreAuth;

import java.util.Map;

public class P6eCloudCoreGroup extends P6eCloudCore {

    private P6eCloudCoreAuth p6eCloudCoreAuth;

    public P6eCloudCoreGroup() { }

    public P6eCloudCoreGroup(P6eCloudCoreAuth p6eCloudCoreAuth) {
        this.p6eCloudCoreAuth = p6eCloudCoreAuth;
    }

    public void onOpen(Object o, Map<String, String> map) {
        if (p6eCloudCoreAuth != null) {
            if (!p6eCloudCoreAuth.execute(map, o)) {
                // guan bi
            }
        }
    }

    public void onClose(Object o) {

    }

    public void onError(Object o, Throwable throwable) {

    }

    public void onMessageText(Object o, String message) {

    }

    public void onMessageBinary(Object o, byte[] message) {

    }

    public void onMessagePong(Object o, byte[] message) {

    }

    public void onMessagePing(Object o, byte[] message) {

    }

    public void onMessageContinuation(Object o, byte[] message) {

    }
}
