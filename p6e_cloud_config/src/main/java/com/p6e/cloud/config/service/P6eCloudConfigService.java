package com.p6e.cloud.config.service;

public class P6eCloudConfigService {

    public static final String MESSAGE_LENGTH_TYPE = "MESSAGE_LENGTH_TYPE";
    public static final String MESSAGE_HEADER_LENGTH_TYPE = "MESSAGE_HEADER_LENGTH_TYPE";
    public static final String MESSAGE_STATUS_TYPE = "MESSAGE_STATUS_TYPE";
    public static final String MESSAGE_TYPE_TYPE = "MESSAGE_TYPE_TYPE";
    public static final String MESSAGE_SPARE_TYPE = "MESSAGE_SPARE_TYPE";
    public static final String MESSAGE_CONTENT_TYPE = "MESSAGE_CONTENT_TYPE";

    public static final String BYTE_TO_INT_MODE = "BYTE_TO_INT_MODE";
    public static final String BYTE_TO_INT_BIG_MODE = "BYTE_TO_INT_BIG_MODE";
    public static final String BYTE_TO_INT_LITTLE_MODE = "BYTE_TO_INT_LITTLE_MODE";
    public static final String CONTENT_NO_DECOMPRESS_MODE = "CONTENT_NO_DECOMPRESS_MODE";
    public static final String CONTENT_ZLIB_DECOMPRESS_MODE = "CONTENT_ZLIB_DECOMPRESS_MODE";

    public static final String INT_TO_BYTE_MODE = "INT_TO_BYTE_MODE";
    public static final String INT_TO_BYTE_BIG_MODE = "INT_TO_BYTE_BIG_MODE";
    public static final String INT_TO_BYTE_LITTLE_MODE = "INT_TO_BYTE_LITTLE_MODE";
    public static final String CONTENT_NO_COMPRESS_MODE = "CONTENT_NO_COMPRESS_MODE";
    public static final String CONTENT_ZLIB_COMPRESS_MODE = "CONTENT_ZLIB_COMPRESS_MODE";

    public static class Data {
        private int len;
        private String mode;
        private String type;

        public Data() { }

        public Data(int len, String mode, String type) {
            this.len = len;
            this.mode = mode;
            this.type = type;
        }

        public int getLen() {
            return len;
        }

        public void setLen(int len) {
            this.len = len;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    private Data[] encoder;
    private Data[] decoder;

    public Data[] getEncoder() {
        return encoder;
    }

    public void setEncoder(Data[] encoder) {
        this.encoder = encoder;
    }

    public Data[] getDecoder() {
        return decoder;
    }

    public void setDecoder(Data[] decoder) {
        this.decoder = decoder;
    }
}
