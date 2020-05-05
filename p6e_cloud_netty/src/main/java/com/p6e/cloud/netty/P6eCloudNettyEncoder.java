package com.p6e.cloud.netty;

import java.util.List;

/**
 * 对发送的数据进行加密操作
 * @author LiDaShuang
 * @version 1.0
 */
public interface P6eCloudNettyEncoder {

    public byte[] execute(Object[] content);
    public byte[] execute(List<Object[]> contentList);

}
