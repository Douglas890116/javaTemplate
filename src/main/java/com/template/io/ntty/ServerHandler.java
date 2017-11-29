package com.template.io.ntty;

import com.template.io.CommonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static Logger log = Logger.getLogger(ServerHandler.class);

    /**
     * 读取客户端信息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf) msg;
        byte[] bytes = new byte[buffer.readableBytes()];
        buffer.readBytes(bytes);
        String message = new String(bytes, "UTF-8");
        log.info("[SERVER]服务端收到信息: " + message);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String result = "["+dateFormat.format(new Date()) + "]"+message;
        // 返回服务端信息
        ctx.write(Unpooled.copiedBuffer(result.getBytes("UTF-8")));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("", cause);
        ctx.close();
    }
}
