package com.p6e.cloud.netty.websocket;

import com.p6e.cloud.netty.P6eCloudNettyAbstract;
import com.p6e.cloud.netty.P6eCloudNettyClient;
import com.p6e.cloud.netty.P6eCloudNettyServiceProcessor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * WebSocket 服务搭建
 * @author LiDaShuang
 * @version 1.0
 */
public class P6eCloudNettyServiceWebSocket extends P6eCloudNettyAbstract {

    /** 注入的日志对象 */
    private static final Logger logger = LoggerFactory.getLogger(P6eCloudNettyServiceWebSocket.class);

    private int port;
    private String contentPath;
    private P6eCloudNettyServiceProcessor processor;

    public P6eCloudNettyServiceWebSocket() {
        this(0);
    }

    public P6eCloudNettyServiceWebSocket(int port) {
        this(port, "/", null);
    }

    public P6eCloudNettyServiceWebSocket(String contentPath) {
        this(0, contentPath, null);
    }

    public P6eCloudNettyServiceWebSocket(P6eCloudNettyServiceProcessor service) {
        this(0, "/", service);
    }

    public P6eCloudNettyServiceWebSocket(int port, String contentPath) {
        this(port, contentPath, null);
    }

    public P6eCloudNettyServiceWebSocket(int port, P6eCloudNettyServiceProcessor service) {
        this(port, "/", service);
    }

    public P6eCloudNettyServiceWebSocket(String contentPath, P6eCloudNettyServiceProcessor service) {
        this(0, contentPath, service);
    }

    public P6eCloudNettyServiceWebSocket(int port, String contentPath, P6eCloudNettyServiceProcessor processor) {
        if (port == 0) port = 20000 + (int) (Math.random() * 45535);
        if (contentPath == null) contentPath = "/";
        if (processor == null) processor = new P6eCloudNettyServiceWebSocketProcessor();
        this.port = port;
        this.processor = processor;
        this.contentPath = contentPath;
    }

    @Override
    public void run() {
        // Netty封装了NIO，Reactor模型，Boss，worker
        // Boss线程
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // Worker线程
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // Netty服务
            // ServerBootstrap   ServerSocketChannel
            ServerBootstrap server = new ServerBootstrap();
            // 链路式编程
            server.group(bossGroup, workerGroup)
                    // 主线程处理类,看到这样的写法，底层就是用反射
                    .channel(NioServerSocketChannel.class)
                    // 子线程处理类 , Handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 客户端初始化处理
                        protected void initChannel(SocketChannel client) {

                            // WebSocket 是基于 Http 协议的，要使用 Http 解编码器
                            client.pipeline().addLast("http-codec", new HttpServerCodec());

                            // 用于大数据流的分区传输
                            client.pipeline().addLast("http-chunked", new ChunkedWriteHandler());

                            // 将多个消息转换为单一的 request 或者 response 对象，最终得到的是 FullHttpRequest 对象
                            client.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));

                            // 处理所有委托管理的 WebSocket 帧类型以及握手本身
                            // 创建 WebSocket 之前会有唯一一次 Http 请求 (Header 中包含 Upgrade 并且值为 websocket)
                            client.pipeline().addLast("http-request", new SimpleChannelInboundHandler<FullHttpRequest>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
                                    try {
                                        String uri = msg.uri();
                                        ctx.fireChannelRead(msg.setUri(contentPath).retain());
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
                                        processor.onOpen(cache.put(ctx.name(), new P6eCloudNettyClient(ctx)), map);
                                    } catch (Exception e) {
                                        processor.onError(cache.get(ctx.name()), e);
                                    }
                                }
                            });
                            // 入参是 ws://server:port/context_path 中的 contex_path
                            client.pipeline().addLast("websocket-server", new WebSocketServerProtocolHandler(contentPath));
                            //业务处理的handler
                            client.pipeline().addLast("frame", new ChannelInboundHandler() {
                                @Override
                                public void channelRegistered(ChannelHandlerContext ctx) {
                                    logger.debug(" channelRegistered " + ctx);
                                }

                                @Override
                                public void channelUnregistered(ChannelHandlerContext ctx) {
                                    logger.debug(" channelUnregistered " + ctx);
                                }

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) {
                                    logger.debug(" channelActive " + ctx);
                                }

                                @Override
                                public void channelInactive(ChannelHandlerContext ctx) {
                                    logger.debug(" channelInactive " + ctx);
                                }

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                    logger.debug(" channelRead " + ctx + "   msg  " + msg);
                                    P6eCloudNettyClient client = cache.get(ctx.name());
                                    try {
                                        WebSocketFrame frame = (WebSocketFrame) msg;
                                        if (frame instanceof BinaryWebSocketFrame) {
                                            ByteBuf byteBuf = frame.content();
                                            byte[] bytes = new byte[byteBuf.readableBytes()];
                                            byteBuf.readBytes(bytes);
                                            processor.onMessageBinary(client, bytes);
                                        } else if (frame instanceof TextWebSocketFrame) {
                                            processor.onMessageText(client, ((TextWebSocketFrame) frame).text());
                                        } else if (frame instanceof PongWebSocketFrame) {
                                            ByteBuf byteBuf = frame.content();
                                            byte[] bytes = new byte[byteBuf.readableBytes()];
                                            byteBuf.readBytes(bytes);
                                            processor.onMessagePong(client, bytes);
                                        } else if (frame instanceof PingWebSocketFrame) {
                                            ByteBuf byteBuf = frame.content();
                                            byte[] bytes = new byte[byteBuf.readableBytes()];
                                            byteBuf.readBytes(bytes);
                                            processor.onMessagePing(client, bytes);
                                        } else if (frame instanceof ContinuationWebSocketFrame) {
                                            ByteBuf byteBuf = frame.content();
                                            byte[] bytes = new byte[byteBuf.readableBytes()];
                                            byteBuf.readBytes(bytes);
                                            processor.onMessageContinuation(client, bytes);
                                        }
                                    } catch (Exception e) {
                                        processor.onError(client, e);
                                    }
                                }

                                @Override
                                public void channelReadComplete(ChannelHandlerContext ctx) {
                                    logger.debug(" channelReadComplete " + ctx);
                                }

                                @Override
                                public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
                                    logger.debug(" userEventTriggered " + ctx);
                                }

                                @Override
                                public void channelWritabilityChanged(ChannelHandlerContext ctx) {
                                    logger.debug(" channelWritabilityChanged " + ctx);
                                }

                                @Override
                                public void handlerAdded(ChannelHandlerContext ctx) {
                                    logger.debug(" handlerAdded " + ctx);
                                }

                                @Override
                                public void handlerRemoved(ChannelHandlerContext ctx) {
                                    logger.debug(" handlerRemoved " + ctx);
                                    cache.remove(ctx.name()); // 删除
                                    processor.onClose(cache.get(ctx.name()));
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                    logger.debug(" exceptionCaught " + ctx);
                                    processor.onError(cache.get(ctx.name()), cause);
                                }
                            });
                        }
                    })
                    // 针对主线程的配置 分配线程最大数量 128
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 针对子线程的配置 保持长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // 启动服务器
            ChannelFuture channelFuture = server.bind(port).sync();
            logger.info("[ NETTY ] ==> WebSocket service started successfully !!");
            logger.info("[ NETTY ] ==> listening port is => [ " + port + " ]");
            logger.info("[ NETTY ] ==> access address is => [ 127.0.0.1:" + port + contentPath + " ]");
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            // 关闭线程池
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
