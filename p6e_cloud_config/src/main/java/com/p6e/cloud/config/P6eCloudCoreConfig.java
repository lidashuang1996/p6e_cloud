package com.p6e.cloud.config;

/**
 * P6eCloudCoreConfig
 * 核心配置文件
 * 不管采用什么方式读取配置信息
 * 最终的必须实现的配置类
 *
 * 1. 读取 Spring YML 文件读取配置信息数据
 * 2. 读取 XXX 配置信息
 * 3. 编程式读取配置信息
 */
public class P6eCloudCoreConfig {

    /** Socket 服务 */
    private P6eCloudCoreConfigService socketService;

    /** Web Socket 服务 */
    private P6eCloudCoreConfigService webSocketService;

}
