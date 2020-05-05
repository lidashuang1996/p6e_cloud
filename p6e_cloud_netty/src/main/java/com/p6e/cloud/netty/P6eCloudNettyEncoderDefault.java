package com.p6e.cloud.netty;

import com.p6e.cloud.common.P6eTool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 对发送的数据进行加密操作的实现类
 * @author LiDaShuang
 * @version 1.0
 */
public class P6eCloudNettyEncoderDefault implements P6eCloudNettyEncoder {

    public static final String MESSAGE_LENGTH_TYPE = "MESSAGE_LENGTH_TYPE";
    public static final String MESSAGE_HEADER_LENGTH_TYPE = "MESSAGE_HEADER_LENGTH_TYPE";
    public static final String MESSAGE_STATUS_TYPE = "MESSAGE_STATUS_TYPE";
    public static final String MESSAGE_TYPE_TYPE = "MESSAGE_TYPE_TYPE";
    public static final String MESSAGE_SPARE_TYPE = "MESSAGE_SPARE_TYPE";
    public static final String MESSAGE_CONTENT_TYPE = "MESSAGE_CONTENT_TYPE";


    public static final String INT_TO_BYTE_MODE = "INT_TO_BYTE_MODE";
    public static final String INT_TO_BYTE_BIG_MODE = "INT_TO_BYTE_BIG_MODE";
    public static final String INT_TO_BYTE_LITTLE_MODE = "INT_TO_BYTE_LITTLE_MODE";

    public static final String CONTENT_NO_COMPRESS_MODE = "CONTENT_NO_COMPRESS_MODE";
    public static final String CONTENT_ZLIB_COMPRESS_MODE = "CONTENT_ZLIB_COMPRESS_MODE";

    public static class Config {
        private int len;
        private String mode;
        private String type;

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

    private Config[] configs;
    public P6eCloudNettyEncoderDefault(Config... configs) {
        this.configs = configs;
    }

    @Override
    public byte[] execute(Object[] content) {
        if (configs.length > content.length) return new byte[0];
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            for (int i = 0; i < configs.length; i++) {
                Config config = configs[i];
                switch (config.getMode()) {
                    case INT_TO_BYTE_BIG_MODE:
                        byteArrayOutputStream.write(P6eTool.intToBytesBig((int) content[i]));
                        break;
                    case INT_TO_BYTE_LITTLE_MODE:
                        byteArrayOutputStream.write(P6eTool.intToBytesLittle((int) content[i]));
                        break;
                    case INT_TO_BYTE_MODE:
                        byteArrayOutputStream.write(new byte[] { 0, (byte) content[i] });
                        break;
                    case CONTENT_ZLIB_COMPRESS_MODE:
                        byteArrayOutputStream.write(P6eTool.compressZlib(((String) content[i]).getBytes(StandardCharsets.UTF_8)));
                        break;
                    case CONTENT_NO_COMPRESS_MODE:
                    default:
                        byteArrayOutputStream.write(((String) content[i]).getBytes(StandardCharsets.UTF_8));
                        break;
                }
            }
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        } finally {
            try {
                if (byteArrayOutputStream != null) byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public byte[] execute(List<Object[]> contentList) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            for (Object[] content : contentList) byteArrayOutputStream.write(execute(content));
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        } finally {
            try {
                if (byteArrayOutputStream != null) byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
