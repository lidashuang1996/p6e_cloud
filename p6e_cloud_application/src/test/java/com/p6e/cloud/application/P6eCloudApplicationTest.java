package com.p6e.cloud.application;

import com.p6e.cloud.config.P6eCloudConfig;
import com.p6e.cloud.config.P6eCloudConfigDefault;
import com.p6e.cloud.core.group.P6eCloudCoreGroupReactor;
import com.p6e.cloud.netty.P6eCloudNettyClient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class P6eCloudApplicationTest {

    private static final Logger logger = LoggerFactory.getLogger(P6eCloudApplicationTest.class);

    @Test
    public void create1() {
        P6eCloudApplication.create(new P6eCloudCoreGroupReactor() {
            @Override
            public void onMessageClientAsync(P6eCloudNettyClient p6eCloudNettyClient, Object[] objects) {
                logger.debug("onMessageClientAsync ==> P6eCloudNettyClient: " + p6eCloudNettyClient + " objects: " + Arrays.toString(objects));
            }

            @Override
            public void onMessageOtherAsync(Object[] objects) {
                logger.debug("onMessageClientAsync ==> objects " + Arrays.toString(objects));
            }

        }).run();
    }

    @Test
    public void create2() {
        P6eCloudConfig cloudConfig = new P6eCloudConfigDefault();
        cloudConfig.getWebSocketService().setPort(11000);
        P6eCloudApplication.create(cloudConfig, new P6eCloudCoreGroupReactor() {

            @Override
            public void onMessageClientAsync(P6eCloudNettyClient p6eCloudNettyClient, Object[] objects) {
                logger.debug("onMessageClientAsync ==> P6eCloudNettyClient: " + p6eCloudNettyClient + " objects: " + Arrays.toString(objects));
            }

            @Override
            public void onMessageOtherAsync(Object[] objects) {
                logger.debug("onMessageClientAsync ==> objects " + Arrays.toString(objects));
            }

        }).run();
    }

    @Test
    public void create3() {
        P6eCloudApplication.create(
            (p6eCloudNettyClient, map, o) -> {
                logger.debug("AUTH : " + p6eCloudNettyClient + " map: " + map);
                return false;
            },
            new P6eCloudCoreGroupReactor() {

                @Override
                public void onMessageClientAsync(P6eCloudNettyClient p6eCloudNettyClient, Object[] objects) {
                    logger.debug("onMessageClientAsync ==> P6eCloudNettyClient: " + p6eCloudNettyClient + " objects: " + Arrays.toString(objects));
                }

                @Override
                public void onMessageOtherAsync(Object[] objects) {
                    logger.debug("onMessageClientAsync ==> objects " + Arrays.toString(objects));
                }

            }).run();
    }

    @Test
    public void create4() {
        P6eCloudConfig cloudConfig = new P6eCloudConfigDefault();
        cloudConfig.getWebSocketService().setPort(11000);
        P6eCloudApplication.create(
            cloudConfig,
            (p6eCloudNettyClient, map, o) -> {
                logger.debug("AUTH : " + p6eCloudNettyClient + " map: " + map);
                return true;
            }, new P6eCloudCoreGroupReactor() {
            @Override
            public void onMessageClientAsync(P6eCloudNettyClient p6eCloudNettyClient, Object[] objects) {
                logger.debug("onMessageClientAsync ==> P6eCloudNettyClient: " + p6eCloudNettyClient + " objects: " + Arrays.toString(objects));
            }

            @Override
            public void onMessageOtherAsync(Object[] objects) {
                logger.debug("onMessageClientAsync ==> objects " + Arrays.toString(objects));
            }
        }).run();
    }
}
