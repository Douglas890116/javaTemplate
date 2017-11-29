package com.template.io.aio.server;

import com.template.io.Properties;

public class Server {
    public volatile static long clientCount = 0;
    private static AsyncServerHandler serverHandle;

    public static void start(){
        start(Properties.socket_server_port);
    }

    public static synchronized void start(int port){
        if (serverHandle!=null) return;
        serverHandle = new AsyncServerHandler(port);
        new Thread(serverHandle,"Server").start();
    }
}
