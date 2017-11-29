package com.template.io.aio.client;

import com.template.io.Properties;

public class Client {
    private static AsyncClientHandler clientHandler;

    public static void start() {
        start(Properties.socket_server_ip, Properties.socket_server_port);
    }

    /**
     * 根据IP 和 端口 发起Socket连接
     * @param ip
     * @param port
     */
    public static void start(String ip, int port) {
        if (clientHandler != null) return;
        clientHandler = new AsyncClientHandler(ip, port);
        new Thread(clientHandler, "Client").start();
    }

    /**
     * 想服务端发送信息
     * @param message 待发送的信息
     * @param charset 信息的编码格式
     */
    public static void sendMessage(String message, String charset) {
        clientHandler.sendMessage(message,charset);
    }
}
