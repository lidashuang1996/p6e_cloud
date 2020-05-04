package com.p6e.cloud.core;

import java.util.Map;

public abstract class P6eCloudCore {

    public static void create() {

    }


    public abstract void onOpen(Object o, Map<String, String> map);

    public abstract void onClose(Object o);

    public abstract void onError(Object o, Throwable throwable);

    public abstract void onMessageText(Object o,String message);

    public abstract void onMessageBinary(Object o, byte[] message);

    public abstract void onMessagePong(Object o, byte[] message);

    public abstract void onMessagePing(Object o, byte[] message);

    public abstract void onMessageContinuation(Object o, byte[] message);

}
