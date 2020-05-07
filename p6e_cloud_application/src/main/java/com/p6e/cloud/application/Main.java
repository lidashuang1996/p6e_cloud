package com.p6e.cloud.application;

import com.p6e.cloud.core.group.P6eCloudCoreGroupReactor;
import com.p6e.cloud.netty.P6eCloudNettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
        P6eCloudApplication.create(new P6eCloudCoreGroupReactor() {
            @Override
            public void onMessageClientAsync(P6eCloudNettyClient p6eCloudNettyClient, Object[] objects) {
                logger.debug("onMessageClientAsync ==> P6eCloudNettyClient: " + p6eCloudNettyClient + " objects: " + Arrays.toString(objects));
                System.out.println("\n--------------");
                System.out.println(p6eCloudNettyClient.getAttribute().get("GROUP_NAME") +
                        "  我是返回的数据 【" + Arrays.toString(objects) + "】 ");
                System.out.println("\n--------------");
                decantMaterial(String.valueOf(p6eCloudNettyClient.getAttribute().get("GROUP_NAME")),
                        "我是返回的数据 【" + Arrays.toString(objects) + "】 ");
            }

            @Override
            public void onMessageOtherAsync(Object[] objects) {
                logger.debug("onMessageClientAsync ==> objects " + Arrays.toString(objects));
            }

        }).run();
    }
}
