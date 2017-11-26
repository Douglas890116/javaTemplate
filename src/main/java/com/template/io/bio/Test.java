package com.template.io.bio;

import java.util.Random;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        // 行服务端
        new Thread(ServerBetter::start).start();

        Thread.sleep(100);

        //运行客户端
//        char operators[] = {'+','-','*','/'};
        Random random = new Random(System.currentTimeMillis());

        new Thread(() -> {
            String message;
            for (int i = 0; i < 5; i++) {
                //随机产生算术表达式
//                String expression = random.nextInt(10)+""+operators[random.nextInt(4)]+(random.nextInt(10)+1);
                message = "[A]"+i;
                Client.send(message);
                try {
                    Thread.currentThread();
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread-A").start();

        new Thread(() -> {
            String message;
            for (int i = 0; i < 5; i++) {
                //随机产生算术表达式
//                String expression = random.nextInt(10)+""+operators[random.nextInt(4)]+(random.nextInt(10)+1);
                message = "[B]"+i;
                Client.send(message);
                try {
                    Thread.currentThread();
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread-B").start();
    }
}
