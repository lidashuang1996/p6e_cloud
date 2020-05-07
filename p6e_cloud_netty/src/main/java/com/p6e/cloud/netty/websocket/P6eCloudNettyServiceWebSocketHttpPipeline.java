package com.p6e.cloud.netty.websocket;

import com.p6e.cloud.common.P6eTool;
import com.p6e.cloud.netty.P6eCloudNettyClient;
import com.p6e.cloud.netty.P6eCloudNettyServiceProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class P6eCloudNettyServiceWebSocketHttpPipeline extends SimpleChannelInboundHandler<FullHttpRequest> {

    // 每个客户端都会有自己的一个 ChannelInboundHandler 处理器
    private String clientId = "CLIENT_ID:" + P6eTool.generateUUID();

    private P6eCloudNettyServiceProcessor processor;

    /** 缓存用户的数量 */
    private Map<String, P6eCloudNettyClient> cache;

    private String contentPath;

    public P6eCloudNettyServiceWebSocketHttpPipeline(P6eCloudNettyServiceProcessor processor,
                                                     Map<String, P6eCloudNettyClient> cache,
                                                     String contentPath) {
        this.processor = processor;
        this.cache = cache;
        this.contentPath = contentPath;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) {
        try {
            String uri = fullHttpRequest.uri();
            ctx.fireChannelRead(fullHttpRequest.setUri(contentPath).retain());
            ctx.flush();
            String[] url = uri.split("\\?");
            Map<String, String> map = new HashMap<>();
            if (url.length == 2) {
                String[] ps = url[1].split("&");
                for (String p : ps) {
                    String[] kv = p.split("=");
                    if (kv.length == 2) map.put(URLDecoder.decode(kv[0], "UTF-8"), URLDecoder.decode(kv[1], "UTF-8"));
                }
            }
            P6eCloudNettyClient nettyClient = new P6eCloudNettyClient(ctx);
            cache.put(clientId, nettyClient);
            processor.onOpen(nettyClient, map);
        } catch (Exception e) {
            processor.onError(cache.get(clientId), e);
        }
    }

    public String clientId() {
        return clientId;
    }

}
