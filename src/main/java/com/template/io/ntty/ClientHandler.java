package com.template.io.ntty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private static Logger log = Logger.getLogger(ClientHandler.class);

    ChannelHandlerContext context;

    /**
     * tcp链路简历成功后调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.context = ctx;
        sendMessage("Client Send Message...", "UTF-8");
    }

    /**
     * 收到服务端信息后调用
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
        log.info("[CLIENT]收到服务端信息: " + message);
    }

    /**
     * 发生异常时调用
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("客户端获取服务端信息发生异常!", cause);
        context.close();
    }

    /**
     * 发送信息
     * @param message 待发送的信息
     * @param charset 信息的编码格式
     */
    public void sendMessage(String message, String charset) {
        log.debug("[CLIENT]客户端发送信息: " + message);
        try {
            byte[] bytes = message.getBytes(charset);
            ByteBuf buffer = Unpooled.buffer(bytes.length);
            buffer.writeBytes(bytes);
            context.writeAndFlush(buffer);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
