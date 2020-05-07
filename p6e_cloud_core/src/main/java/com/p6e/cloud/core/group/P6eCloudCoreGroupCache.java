package com.p6e.cloud.core.group;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

final class P6eCloudCoreGroupCache {

    private static final Map<String, P6eCloudCoreGroupCacheModel> cache = new HashMap<>();
    private static ThreadPoolExecutor executor;
    static void init(ThreadPoolExecutor threadPoolExecutor) {
        executor = threadPoolExecutor;
    }

    static P6eCloudCoreGroupCacheModel createGroupCacheModel(String group) {
        P6eCloudCoreGroupCacheModel p6eCloudCoreGroupCacheModel = cache.get(group);
        if (p6eCloudCoreGroupCacheModel == null) {
            p6eCloudCoreGroupCacheModel = new P6eCloudCoreGroupCacheModel(executor);
            cache.put(group, p6eCloudCoreGroupCacheModel);
            return p6eCloudCoreGroupCacheModel;
        }
        return p6eCloudCoreGroupCacheModel;
    }

    static P6eCloudCoreGroupCacheModel getGroupCacheModel(String group) {
        return cache.get(group);
    }

}
