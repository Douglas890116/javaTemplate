package com.template.io.aio.server;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadHandler implements CompletionHandler<Integer, ByteBuffer>{
    private static Logger log = Logger.getLogger(ReadHandler.class);
    //用于读取半包消息和发送应答
    private AsynchronousSocketChannel channel;

    public ReadHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    /**
     * 读取到消息后的处理
     * @param result
     * @param attachment
     */
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        try {
            // flip操作
            attachment.flip();
            byte[] bytes = new byte[attachment.remaining()];
            attachment.get(bytes);
            String message = new String(bytes, "UTF-8");
            log.info("[SERVER]服务端收到信息: " + message);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String returnMsg = "["+dateFormat.format(new Date()) + "]"+message;
            // 返回服务端信息
            doWrite(returnMsg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("completed(Integer result, ByteBuffer attachment) 方法错误!", e);
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        log.error("消息读取失败!!!", exc);
        try {
            channel.close();
        } catch (IOException e) {
            log.error("failed(Throwable exc, ByteBuffer attachment) 方法错误!", e);
            e.printStackTrace();
        }
    }

    /**
     * 写操作
     * @param message
     * @param charset
     */
    private void doWrite(String message, String charset) throws UnsupportedEncodingException {
        byte[] bytes = message.getBytes(charset);
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        //异步写数据 参数与前面的read一样
        channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                //如果没有发送完，就继续发送直到完成
                if (attachment.hasRemaining()) {
                    channel.write(attachment, attachment, this);
                } else {
                    //创建新的Buffer
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    //异步读  第三个参数为接收消息回调的业务Handler
                    channel.read(readBuffer, readBuffer, new ReadHandler(channel));
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                log.error("信息发送失败!!!", exc);
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
