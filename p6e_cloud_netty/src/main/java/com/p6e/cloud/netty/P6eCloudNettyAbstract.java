package com.p6e.cloud.netty;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

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

    /** 缓存用户的数量 */
    protected Map<String, P6eCloudNettyClient> cache = new Hashtable<>();

    public P6eCloudNettyAbstract() {
        new Thread(){ // 创建一个线程取执行心跳保护
            @Override
            public void run() {
                super.run();
                try {
                    long currentDate = new Date().getTime();
                    for (String key : cache.keySet()) {
                        try {
                            P6eCloudNettyClient p6eCloudNettyClient = cache.get(key);
                            if (p6eCloudNettyClient == null) {
                                cache.remove(key);
                            } else {
                                if (currentDate - p6eCloudNettyClient.__date__() > 60000) {
                                    cache.remove(key);
                                    p6eCloudNettyClient.close();
                                }
                            }
                        } catch (Exception e) {
                            P6eCloudNettyClient p6eCloudNettyClient = cache.remove(key);
                            if (p6eCloudNettyClient != null) p6eCloudNettyClient.close();
                            e.printStackTrace();
                        }
                    }
                    Thread.sleep(60000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public abstract void run();

}
