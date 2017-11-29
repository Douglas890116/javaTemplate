package com.template.io.aio.client;

import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.Count;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    private static Logger log = Logger.getLogger(ReadHandler.class);

    private AsynchronousSocketChannel channel;
    private CountDownLatch countDownLatch;

    public ReadHandler(AsynchronousSocketChannel channel, CountDownLatch countDownLatch) {
        this.channel = channel;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        try {
            attachment.flip();
            byte[] bytes = new byte[attachment.remaining()];
            attachment.get(bytes);
            String message = new String(bytes, "UTF-8");
            log.info("[Client]收到服务端信息: " + message);
        } catch (UnsupportedEncodingException e) {
            log.error("completed(Integer result, ByteBuffer attachment) 方法错误!", e);
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        log.error("数据读取失败!!!", exc);
        try {
            channel.close();
            countDownLatch.countDown();
        } catch (IOException e) {
            log.error("failed(Throwable exc, ByteBuffer attachment) 方法错误!", e);
            e.printStackTrace();
        }
    }
}
