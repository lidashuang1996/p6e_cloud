package com.p6e.cloud.core.group;

import com.p6e.cloud.core.P6eCloudCoreReactor;
import com.p6e.cloud.netty.P6eCloudNettyClient;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * P6eCloud 组的消息模式的反应堆
 *
 * 用来处理推送消息到该组的所有客户端
 * @author LiDaShuang
 * @version 1.0
 */
public abstract class P6eCloudCoreGroupReactor implements P6eCloudCoreReactor {

    private ThreadPoolExecutor executor;

    public P6eCloudCoreGroupReactor() {
        this(25, 5);
    }

    public P6eCloudCoreGroupReactor(int pThreads, int rThreads) {
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(rThreads);
        P6eCloudCoreGroupCache.init((ThreadPoolExecutor) Executors.newFixedThreadPool(pThreads));
    }

    /**
     * 推送消息到连接该组的客户端
     * @param group 组的 ID
     * @param message 消息的内容
     */
    public void decantMaterial(String group, String message) {
        P6eCloudCoreGroupCacheModel p6eCloudCoreGroupCacheModel = P6eCloudCoreGroupCache.getGroupCacheModel(group);
        if (p6eCloudCoreGroupCacheModel != null) p6eCloudCoreGroupCacheModel.pushMessage(message);
    }

    @Override
    public void onMessageClient(P6eCloudNettyClient client, Object[] message) {
        executor.execute(() -> onMessageClientAsync(client, message));
    }

    @Override
    public void onMessageOther(Object[] message) {
        executor.execute(() -> onMessageOtherAsync(message));
    }

    public abstract void onMessageClientAsync(P6eCloudNettyClient client, Object[] message);

    public abstract void onMessageOtherAsync(Object[] message);

}
