package com.hef.nio.gateway.inbound;

import com.hef.nio.gateway.outbound.httpclient.HttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.ReferenceCountUtil;

import java.util.logging.Logger;

/**
 * @author lifei
 * @since 2021/3/7
 */
public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = Logger.getLogger("com.hef.nio.gateway.inbound.HttpInboundHandler");

    private final String proxyServer;
    private final HttpOutboundHandler outboundHandler;

    public HttpInboundHandler(String proxyServer){
        this.proxyServer = proxyServer;
        this.outboundHandler = new HttpOutboundHandler(proxyServer);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
            outboundHandler.handle(fullHttpRequest, ctx);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
