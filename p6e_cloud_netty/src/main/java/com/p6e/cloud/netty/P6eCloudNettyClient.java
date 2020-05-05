package com.p6e.cloud.netty;

import io.netty.channel.ChannelHandlerContext;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class P6eCloudNettyClient {
    private long __date__;
    private Map<String, Object> attribute;
    private ChannelHandlerContext channelHandlerContext;

    public P6eCloudNettyClient(ChannelHandlerContext ctx) {
        this.channelHandlerContext = ctx;
        this.__date__ = new Date().getTime();
        this.attribute = new HashMap<>();
    }

    public long __date__() {
        return __date__;
    }

    public Map<String, Object> getAttribute() {
        return attribute;
    }

    public Iterator<String> getAttributeName() {
        return attribute.keySet().iterator();
    }

    public Object getAttribute(String key) {
        return attribute.get(key);
    }

    public void setAttribute(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    public void setAttribute(String key, Object value) {
        this.attribute.put(key, value);
    }

    public void close() {
    }

    public void sendMessage(String content) {

    }
}
