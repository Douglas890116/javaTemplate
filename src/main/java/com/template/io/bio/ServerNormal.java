package com.template.io.bio;

import com.template.io.CommonUtil;
import com.template.io.Properties;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerNormal {
    private static Logger log = Logger.getLogger(ServerNormal.class);

    //单例的ServerSocket
    private static ServerSocket server;

    /**
     * 若没有端口参数，则使用默认端口
     */
    public static void start() {
        start(Properties.socket_server_port);
    }

    /**
     * 根据传入的端口进行监听
     *
     * @param port 监听的端口号
     */
    public synchronized static void start(int port) {
        if (server != null) return;
        try {
            //通过构造函数创建ServerSocket
            //如果端口合法且空闲，服务端就监听成功
            server = new ServerSocket(port);
            log.info("服务已启动, 监听: " + port);

            Socket socket;
            //通过无线循环监听客户端连接
            //如果没有客户端接入，将阻塞在accept操作上
            while (true) {
                socket = server.accept();
                //当有新的客户端接入时，会执行下面的代码
                //然后创建一个新的线程处理这条Socket链路
                new Thread(new ServerHandler(socket)).start();
            }
        } catch (IOException e) {
            log.error("start(int port) 方法错误!", e);
            e.printStackTrace();
        } finally {
            try {
                CommonUtil.closeServerSocket(server);
            } catch (IOException e) {
                e.printStackTrace();
            }
            server = null;
            log.info("服务端已关闭");
        }
    }
}
