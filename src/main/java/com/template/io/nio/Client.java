package com.template.io.nio;

import com.template.io.Properties;
import org.apache.log4j.Logger;

public class Client {
    private static Logger log = Logger.getLogger(ClientHandler.class);

    private static ClientHandler clientHandler;

    public static void start() {
        start(Properties.socket_server_ip, Properties.socket_server_port);
    }
    public static void start(String ip, int port) {
        if (clientHandler != null) clientHandler.stop();
        clientHandler = new ClientHandler(ip, port);
        new Thread(clientHandler, "Client").start();
    }

    public static void sendMessage(String message, String charset) {
        clientHandler.sendMessage(message, charset);
    }
}
