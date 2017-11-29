package com.template.io.ntty;

import com.template.io.Properties;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

import java.util.Scanner;

public class Client implements Runnable {
    private static Logger log = Logger.getLogger(Client.class);

    static ClientHandler client = new ClientHandler();

    @Override
    public void run() {
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(client);
                }
            });
            ChannelFuture f = bootstrap.connect(Properties.socket_server_ip, Properties.socket_server_port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workGroup.shutdownGracefully();
        }
    }

    public void sendMessage(String message, String charset) {
        client.sendMessage(message, charset);
    }

    public static void main(String[] args) throws Exception {
        new Thread(new Client()).start();
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.err.println("请输入信息: ");
            client.sendMessage(scanner.nextLine(), "UTF-8");
        }
    }
}
