package com.template.io.aio.client;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AsyncClientHandler implements CompletionHandler<Void, AsyncClientHandler>, Runnable {
    private static Logger log = Logger.getLogger(AsyncClientHandler.class);

    private String ip;
    private int port;
    private CountDownLatch countDownLatch;
    private AsynchronousSocketChannel channel;

    public AsyncClientHandler(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            //创建异步的客户端通道
            this.channel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            log.error("AsyncClientHandler(String ip, int port) 方法错误!", e);
            e.printStackTrace();
        }
    }

    @Override
    public void completed(Void result, AsyncClientHandler attachment) {
        //连接服务器成功
        //意味着TCP三次握手完成
        log.info("链接服务端成功...");
    }

    @Override
    public void failed(Throwable exc, AsyncClientHandler attachment) {
        log.error("链接服务端失败!!!", exc);
    }

    @Override
    public void run() {
        try {
            //创建CountDownLatch等待
            countDownLatch = new CountDownLatch(1);
            //发起异步连接操作，回调参数就是这个类本身，如果连接成功会回调completed方法
            channel.connect(new InetSocketAddress(ip, port), this, this);

            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("run() 方法错误!", e);
            e.printStackTrace();
        }
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 发送消息
     * @param message 待发送的消息
     * @param charset 消息的编码格式
     */
    public void sendMessage(String message, String charset) {
        try {
            byte[] bytes = message.getBytes(charset);
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            channel.write(buffer, buffer, new WriteHandler(channel, countDownLatch));
        } catch (UnsupportedEncodingException e) {
            log.error("sendMessage(String message, String charset) 方法错误!", e);
            e.printStackTrace();
        }
    }
}
