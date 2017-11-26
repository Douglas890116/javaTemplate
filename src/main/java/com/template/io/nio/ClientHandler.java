package com.template.io.nio;

import com.template.io.CommonUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ClientHandler implements Runnable{
    private static Logger log = Logger.getLogger(ClientHandler.class);
    /**
     * 服务端IP
     */
    private String ip;
    /**
     * 服务端端口
     */
    private int port;
    /**
     * 多路复用器
     */
    private Selector selector;
    /**
     * Socket通道
     */
    private SocketChannel channel;
    /**
     * 服务状态: true - 正常, false - 阻塞
     */
    private volatile boolean status;

    public ClientHandler(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            // 打开选择器
            selector = Selector.open();
            // 打开socket监听通道
            channel = SocketChannel.open();
            // 设置通道为非阻塞模式, true为阻塞模式, false为非阻塞模式
            channel.configureBlocking(false);
            // 设置服务状态为true
            status = true;
        } catch (IOException e) {
            log.error("ClientHandler(String ip, int port) 方法错误!", e);
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            connect();
            while (status) {
                selector.select(1000);

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                SelectionKey key = null;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    inputHandler(key);
                }
            }
        } catch (IOException e) {
            log.error("run() 方法错误!", e);
            e.printStackTrace();
        }
        //selector关闭后会自动释放里面管理的资源
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop(){
        status = false;
    }

    /**
     *
     * @param key
     * @throws IOException
     */
    private void inputHandler(SelectionKey key) throws IOException {
        if (key.isValid()) {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                if (socketChannel.finishConnect());
                else System.exit(1);
            }
            if (key.isReadable()) {
                // 创建ByteBuffer，并开辟一个1M的缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                // 读取请求，返回读取到的字节数
                int size = socketChannel.read(buffer);
                if (size > 0) {
                    // 将缓冲区当前的limit设置为position=0，用于后续对缓冲区的读取操作
                    buffer.flip();
                    // 根据缓冲区可读字节数创建字节数组
                    byte[] bytes = new byte[buffer.remaining()];
                    // 将数据读到字节数组中
                    buffer.get(bytes);

                    String message = new String(bytes, "UTF-8");
                    log.info("收到服务器返回的信息: " + message);
                } else {
                    key.cancel();
                    socketChannel.close();
                }
            }
        }
    }

    public void sendMessage(String message, String charset) {
        try {
            channel.register(selector, SelectionKey.OP_WRITE);
            CommonUtil.doWrite(channel, message, charset);
        } catch (ClosedChannelException e) {
            log.error("sendMessage(String message) 方法错误!", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("sendMessage(String message) 方法错误!", e);
            e.printStackTrace();
        }

    }

    /**
     * 进行链接
     * @throws IOException
     */
    private void connect() throws IOException {
        if (channel.connect(new InetSocketAddress(ip, port)));
        else channel.register(selector, SelectionKey.OP_CONNECT);
    }
}
