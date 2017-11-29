package com.template.io.aio.client;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class WriteHandler implements CompletionHandler<Integer, ByteBuffer>{
    private static Logger log = Logger.getLogger(WriteHandler.class);

    private AsynchronousSocketChannel channel;
    private CountDownLatch countDownLatch;

    public WriteHandler(AsynchronousSocketChannel channel, CountDownLatch countDownLatch) {
        this.channel = channel;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if (attachment.hasRemaining()) {
            channel.write(attachment, attachment, this);
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.read(buffer, buffer, new ReadHandler(channel, countDownLatch));
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        log.error("数据发送失败!!!", exc);
        try {
            channel.close();
            countDownLatch.countDown();
        } catch (IOException e) {
            log.error("failed(Throwable exc, ByteBuffer attachment) 方法错误!", e);
            e.printStackTrace();
        }
    }
}
