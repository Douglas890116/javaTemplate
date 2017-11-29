package com.template.io.aio;

import com.template.io.aio.client.Client;
import com.template.io.aio.server.Server;

import java.util.Scanner;

import static com.template.io.aio.client.Client.*;

public class Test {
    public static void main(String[] args) {
        try {
            // 启动服务端
            Server.start();
            // 睡眠1秒 防止客户端先于服务端启动
            Thread.sleep(1000);
            // 启动客户端
            start();
            Scanner input = new Scanner(System.in);
            String message;
            while(true) {
                System.err.print("请输入你的消息: ");
                message = input.nextLine();
                Client.sendMessage(message, "Utf-8");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
