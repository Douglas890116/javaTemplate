package com.template.io.ntty;

import com.template.io.Properties;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        try {
            new Server(Properties.socket_server_port).start();
//
//            Client client = new Client();
//            new Thread(client, "Client").start();
//
//            String message;
//            Scanner input = new Scanner(System.in);
//            while (true) {
//                System.err.println("请输入你的信息: ");
//                message = input.nextLine();
//                client.sendMessage(message, "UTF-8");
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}