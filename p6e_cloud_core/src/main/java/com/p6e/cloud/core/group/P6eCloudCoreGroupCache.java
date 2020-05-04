package com.p6e.cloud.core.group;

import java.util.HashMap;
import java.util.Map;

public final class P6eCloudCoreGroupCache {

    private static final Map<String, P6eCloudCoreGroupCacheModel> cache = new HashMap<>();

    public static P6eCloudCoreGroupCacheModel getGroupCacheModel(String group) {
        return cache.get(group);
    }
}
