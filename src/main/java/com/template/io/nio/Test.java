package com.template.io.nio;

import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        Server.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Client.start();

        String message = "";
        while (!message.equals("NO")) {
            Scanner input = new Scanner(System.in);
            System.err.print("请输入信息: ");
            message = input.next();
            Client.sendMessage(message, "UTF-8");
        }
    }
}
