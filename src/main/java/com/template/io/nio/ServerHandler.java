package com.template.io.nio;

import com.template.io.CommonUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class ServerHandler implements Runnable {
    private static Logger log = Logger.getLogger(ServerHandler.class);
    /**
     * 多路复用器
     */
    private Selector selector;
    /**
     * SocketServer通道
     */
    private ServerSocketChannel serverChannel;
    /**
     * 状态: true - 正常, false - 停止
     */
    private volatile boolean status;

    public ServerHandler(int port) {
        try {
            // 打开选择器
            selector = Selector.open();
            // 打开监听通道
            serverChannel = ServerSocketChannel.open();
            // 设置通道为非阻塞模式, true - 为阻塞模式, false - 为非阻塞模式
            serverChannel.configureBlocking(false);
            // 绑定端口, 设置backlog设为1024
            serverChannel.socket().bind(new InetSocketAddress(port), 1024);
            // 注册多路复用器, 开始监听端口
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            // 设置服务状态为true
            status = true;
        } catch (IOException e) {
            log.error("ServerHandler(int port) 方法错误!", e);
            e.printStackTrace();
        }
    }

    public void stop() {
        status = false;
    }
    @Override
    public void run() {
        while (status) {
            try {
                // 无论是否有读写事件发生，selector每隔1s被唤醒一次
                selector.select(1000);
                Set<SelectionKey> keys =  selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                SelectionKey key = null;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    inputHandler(key);
                }
            } catch (IOException e) {
                log.error("run() 方法错误!", e);
                e.printStackTrace();
            }
        }
        // selector关闭后会自动释放里面管理的资源
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                log.error("run() 方法错误!", e);
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param key
     * @throws IOException
     */
    private void inputHandler(SelectionKey key) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        if (key.isValid()) {
            // 处理新接入的请求消息
            if (key.isAcceptable()) {
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                // 通过ServerSocketChannel的accept创建SocketChannel实例
                // 完成该操作意味着完成TCP三次握手，TCP物理链路正式建立
                SocketChannel socketChannel = serverSocketChannel.accept();
                // 将socketChannel 设置为非阻塞
                socketChannel.configureBlocking(false);
                // 将选择器注册为读
                socketChannel.register(selector, SelectionKey.OP_READ);
            }
            // 开始读去信息
            if (key.isReadable()) {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                // 创建ByteBuffer，并开辟一个1M的缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                // 读取请求码流，返回读取到的字节数
                int size = socketChannel.read(buffer);
                if (size > 0) {
                    // 将缓冲区当前的limit设置为position=0
                    // 或者理解为将读取的指针指向开始
                    // 用于后续对缓冲区的读取操作
                    buffer.flip();
                    // 根据缓冲区可读字节数创建字节数组
                    byte[] bytes = new byte[buffer.remaining()];
                    // 将缓冲区可读字节数组复制到新建的数组中
                    buffer.get(bytes);
                    String message = new String(bytes, "UTF-8");
                    log.info("服务端收到信息: " + message);

                    String result = "["+dateFormat.format(new Date()) + "]"+message;
                    // 返回服务端信息
                    CommonUtil.doWrite(socketChannel, result, "UTF-8");

                }
            }
        }
    }
}
