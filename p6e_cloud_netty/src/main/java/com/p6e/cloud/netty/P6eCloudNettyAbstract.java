package com.p6e.cloud.netty;

public abstract class P6eCloudNettyAbstract {

    private static P6eCloudNettyEncoder p6eCloudNettyEncoder;
    private static P6eCloudNettyDecoder p6eCloudNettyDecoder;

    public static void init(P6eCloudNettyEncoder encoder, P6eCloudNettyDecoder decoder) {
        p6eCloudNettyEncoder = encoder;
        p6eCloudNettyDecoder = decoder;
    }

    public static P6eCloudNettyDecoder getP6eCloudNettyDecoder() {
        return p6eCloudNettyDecoder;
    }

    public static P6eCloudNettyEncoder getP6eCloudNettyEncoder() {
        return p6eCloudNettyEncoder;
    }

    public abstract void run();

}
