package com.template.io.bio;

import com.template.io.Properties;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private static Logger log = Logger.getLogger(Client.class);

    /**
     * 如果没有IP，端口，就发送默认地址
     * @param message
     */
    public static void send(String message) {
        send(Properties.socket_server_ip, Properties.socket_server_port, message);
    }
    /**
     * 客户端向服务器发送消息
     * @param ip      服务器IP
     * @param port    服务器端口
     * @param message 需要发送的消息
     */
    public static void send(String ip, int port, String message) {
        log.info("需要发送的信息: " + message);
        Socket socket = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            socket = new Socket(ip, port);
            log.debug("创建Socket成功");

            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println(message);
            log.debug("Socket发送信息成功");

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            log.info("收到服务器返回的信息: " + reader.readLine());
        } catch (UnknownHostException e) {
            log.error("send(String ip, int port, String message) 方法错误!", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.error("send(String ip, int port, String message) 方法错误!", e);
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
