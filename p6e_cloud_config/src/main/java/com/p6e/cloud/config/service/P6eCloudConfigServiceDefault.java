package com.p6e.cloud.config.service;

public class P6eCloudConfigServiceDefault extends P6eCloudConfigService {
    public P6eCloudConfigServiceDefault() {
        // 编码
        this.setEncoder(new Data[] {
                new Data(4, MESSAGE_LENGTH_TYPE, INT_TO_BYTE_BIG_MODE),
                new Data(2, MESSAGE_HEADER_LENGTH_TYPE, INT_TO_BYTE_MODE),
                new Data(2, MESSAGE_STATUS_TYPE, INT_TO_BYTE_MODE),
                new Data(4, MESSAGE_TYPE_TYPE, INT_TO_BYTE_BIG_MODE),
                new Data(4, MESSAGE_SPARE_TYPE, INT_TO_BYTE_BIG_MODE),
                new Data(4, MESSAGE_CONTENT_TYPE, CONTENT_NO_COMPRESS_MODE),
        });

        // 解码
        this.setDecoder(new Data[] {
                new Data(4, MESSAGE_LENGTH_TYPE, BYTE_TO_INT_BIG_MODE),
                new Data(2, MESSAGE_HEADER_LENGTH_TYPE, BYTE_TO_INT_MODE),
                new Data(2, MESSAGE_STATUS_TYPE, BYTE_TO_INT_MODE),
                new Data(4, MESSAGE_TYPE_TYPE, BYTE_TO_INT_BIG_MODE),
                new Data(4, MESSAGE_SPARE_TYPE, BYTE_TO_INT_BIG_MODE),
                new Data(4, MESSAGE_CONTENT_TYPE, CONTENT_NO_DECOMPRESS_MODE),
        });
    }
}
