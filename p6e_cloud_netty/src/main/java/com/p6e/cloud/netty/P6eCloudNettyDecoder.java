package com.p6e.cloud.netty;

import java.util.List;

/**
 * 对接收的数据进行解密操作
 * @author LiDaShuang
 * @version 1.0
 */
public interface P6eCloudNettyDecoder {

    public List<Object[]> execute(byte[] bytes);

}
