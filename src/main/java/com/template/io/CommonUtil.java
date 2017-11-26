package com.template.io;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class CommonUtil {
    /**
     * 进行输出
     * @param channel
     * @param message
     * @param charset
     * @throws IOException
     */
    public static void doWrite(SocketChannel channel, String message, String charset) throws IOException {
        // 将消息转为字节数组
        byte[] bytes = message.getBytes(charset);
        // 根据数组容量创建ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        // 将数组复制到buffer
        buffer.put(bytes);
        // flip操作
        buffer.flip();
        // 发送数据
        channel.write(buffer);
        // 此处没有做半包处理
    }

    /**
     * 关闭ServerSocket
     * @param server
     */
    public static void closeServerSocket(ServerSocket server) throws IOException {
        if (server != null) server.close();
    }
}
