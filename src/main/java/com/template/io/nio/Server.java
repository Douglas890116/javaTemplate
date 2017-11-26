package com.template.io.nio;

import com.template.io.Properties;
import org.apache.log4j.Logger;

public class Server {
    private static Logger log = Logger.getLogger(Server.class);
    private static ServerHandler serverHandle;

    public static void start() {
        start(Properties.socket_server_port);
    }

    public static synchronized void start(int port) {
        if (serverHandle != null) serverHandle.stop();
        serverHandle = new ServerHandler(port);
        new Thread(serverHandle, "serverHandler").start();
    }
}
