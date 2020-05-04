package com.p6e.cloud.core.group;

/**
 * P6eCloud 组的消息模式的反应堆
 *
 * 用来处理推送消息到该组的所有客户端
 * @author LiDaShuang
 * @version 1.0
 */
public final class P6eCloudCoreGroupReactor {

    /**
     * 推送消息到连接该组的客户端
     * @param group 组的 ID
     * @param bytes 消息的内容
     */
    public static void decantMaterial(String group, byte[] bytes) {
        P6eCloudCoreGroupCacheModel p6eCloudCoreGroupCacheModel = P6eCloudCoreGroupCache.getGroupCacheModel(group);
        if (p6eCloudCoreGroupCacheModel != null) p6eCloudCoreGroupCacheModel.pushMessage(bytes);
    }

    /*
     * 预留集群模式处理消息的方法位置
     * ...
     * ...
     * ...
     */

}
