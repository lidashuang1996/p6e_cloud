package com.p6e.cloud.core.group;

import com.p6e.cloud.core.P6eCloudCore;
import com.p6e.cloud.core.P6eCloudCoreAuth;
import com.p6e.cloud.netty.P6eCloudNettyClient;

import java.util.Map;

public class P6eCloudCoreGroup extends P6eCloudCore {

    private static final String GROUP_NAME = "GROUP_NAME";
    private static final String COORDINATE_NAME = "COORDINATE_NAME";

    private P6eCloudCoreAuth p6eCloudCoreAuth;
    private P6eCloudCoreGroupReactor p6eCloudCoreGroupReactor;

    public P6eCloudCoreGroup() { }

    public P6eCloudCoreGroup(P6eCloudCoreAuth p6eCloudCoreAuth) {
        this.p6eCloudCoreAuth = p6eCloudCoreAuth;
    }

    public P6eCloudCoreGroup(P6eCloudCoreGroupReactor p6eCloudCoreGroupReactor) {
        this.p6eCloudCoreGroupReactor = p6eCloudCoreGroupReactor;
    }

    public P6eCloudCoreGroup(P6eCloudCoreAuth p6eCloudCoreAuth,
                             P6eCloudCoreGroupReactor p6eCloudCoreGroupReactor) {
        this.p6eCloudCoreAuth = p6eCloudCoreAuth;
        this.p6eCloudCoreGroupReactor = p6eCloudCoreGroupReactor;
    }

    @Override
    public void onOpen(P6eCloudNettyClient client, Map<String, String> map, Object message) {
        if (p6eCloudCoreAuth != null) {
            if (!p6eCloudCoreAuth.execute(client, map, message)) client.close();
        }
        String group = map.get("group");
        if (group == null) {
            client.close();
            throw new RuntimeException(this.getClass().toString() + " group data is null");
        } else {
            client.setAttribute(GROUP_NAME, group);
            int[] coordinate = P6eCloudCoreGroupCache.createGroupCacheModel(group).add(client);
            client.setAttribute(COORDINATE_NAME, coordinate);

            System.out.println("\n\n");
            System.out.println("------------------");
            System.out.println(client.getAttribute(GROUP_NAME));
            System.out.println(((int[])client.getAttribute(COORDINATE_NAME))[0] + "  " + ((int[])client.getAttribute(COORDINATE_NAME))[1]);
            System.out.println(P6eCloudCoreGroupCache.getGroupCacheModel(group).cache());
            System.out.println("------------------");
            System.out.println("\n\n");
        }
    }

    @Override
    public void onClose(P6eCloudNettyClient client) {
        P6eCloudCoreGroupCache
                .getGroupCacheModel(String.valueOf(client.getAttribute(GROUP_NAME)))
                .del((int[]) client.getAttribute(COORDINATE_NAME));
    }

    @Override
    public void onError(P6eCloudNettyClient client, Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onMessage(P6eCloudNettyClient client, Object[] message) {
        p6eCloudCoreGroupReactor.onMessageClient(client, message);
    }

}
