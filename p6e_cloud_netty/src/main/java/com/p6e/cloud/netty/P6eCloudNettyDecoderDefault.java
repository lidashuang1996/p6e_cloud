package com.p6e.cloud.netty;

import com.p6e.cloud.common.P6eTool;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 对发送的数据进行解密操作的实现类
 * @author LiDaShuang
 * @version 1.0
 */
public class P6eCloudNettyDecoderDefault implements P6eCloudNettyDecoder {
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
    public P6eCloudNettyDecoderDefault(Config... configs) {
        this.configs = configs;
    }

    @Override
    public List<Object[]> execute(byte[] bytes) {
        List<Object[]> result = new ArrayList<>();
        if (configs == null || configs.length == 0) result.add(new Object[] { bytes });
        else {
            Config config = configs[0];
            if (MESSAGE_LENGTH_TYPE.equals(config.getType())) {
                int index = 0;
                while (bytes.length > config.getLen() + index) {
                    int len = analysis(interceptBytes(bytes, index, config.getLen()), config.getMode());
                    int headerLength = config.getLen();
                    index += config.getLen();
                    if (len > 0 && bytes.length >= index + len) {
                        Object[] o = new Object[configs.length];
                        o[0] = len;
                        for (int i = 1; i < configs.length; i++) {
                            Config item = configs[i];
                            if (MESSAGE_CONTENT_TYPE.equals(item.getType())) {
                                o[i] = analysisContent(interceptBytes(bytes, index, len - headerLength), item.getMode());
                                index += len - headerLength;
                            } else {
                                o[i] = analysis(interceptBytes(bytes, index, item.getLen()), item.getMode());
                                headerLength += item.getLen();
                                index += item.getLen();
                            }
                        }
                        result.add(o);
                    } else break;
                }
            } else result.add(new Object[] { bytes });
        }
        return result;
    }

    private byte[] interceptBytes(byte[] bytes, int index, int len) {
        byte[] result = new byte[len];
        System.arraycopy(bytes, index, result, 0, len);
        return result;
    }

    private int analysis(byte[] bytes, String mode) {
        switch (mode) {
            case BYTE_TO_INT_BIG_MODE:
                return P6eTool.bytesToIntBig(bytes);
            case BYTE_TO_INT_LITTLE_MODE:
                return P6eTool.bytesToIntLittle(bytes);
            case BYTE_TO_INT_MODE:
                int t = 0;
                for (byte b : bytes) t += (int) b;
                return t;
            default:
                return 0;
        }
    }

    private String analysisContent(byte[] bytes, String mode) {
        switch (mode) {
            case CONTENT_ZLIB_DECOMPRESS_MODE:
                return new String(P6eTool.decompressZlib(bytes), StandardCharsets.UTF_8);
            case CONTENT_NO_DECOMPRESS_MODE:
            default:
                return new String(bytes, StandardCharsets.UTF_8);
        }
    }

}
