package com.template.io.aio.server;

import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncServerHandler> {
    Logger log = Logger.getLogger(AcceptHandler.class);

    @Override
    public void completed(AsynchronousSocketChannel result, AsyncServerHandler attachment) {
        //继续接受其他客户端的请求
        Server.clientCount++;
        System.out.println("[SERVER]连接的客户端数：" + Server.clientCount);
        attachment.channel.accept(attachment, this);
        //创建新的Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //异步读  第三个参数为接收消息回调的业务Handler
        result.read(buffer, buffer, new ReadHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsyncServerHandler attachment) {
        log.error("[SERVER]服务端接受信息失败!", exc);
        attachment.countDownLatch.countDown();
    }
}
