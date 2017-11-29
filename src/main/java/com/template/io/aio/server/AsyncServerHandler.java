package com.template.io.aio.server;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AsyncServerHandler implements Runnable {
    private static Logger log = Logger.getLogger(AsyncServerHandler.class);

    public AsynchronousServerSocketChannel channel;
    public CountDownLatch countDownLatch;

    public AsyncServerHandler(int port) {
        try {
            //创建服务端通道
            channel = AsynchronousServerSocketChannel.open();
            //绑定端口
            channel.bind(new InetSocketAddress(port));
            log.info("[SERVER]服务端已启动, 监听: " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            //CountDownLatch初始化
            //它的作用：在完成一组正在执行的操作之前，允许当前的现场一直阻塞
            //此处，让现场在此阻塞，防止服务端执行完成后退出
            //也可以使用while(true)+sleep
            //生成环境就不需要担心这个问题，因为服务端是不会退出的
            countDownLatch = new CountDownLatch(1);
            //用于接收客户端的连接
            channel.accept(this,new AcceptHandler());
            countDownLatch.await();
        } catch (InterruptedException e) {
            log.error("run() 方法错误!", e);
            e.printStackTrace();
        }
    }
}
