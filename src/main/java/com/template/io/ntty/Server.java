package com.template.io.ntty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;

public class Server {
    private Logger log = Logger.getLogger(Server.class);

    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(mainGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ServerHandler());
                    }
                });
            ChannelFuture f = serverBootstrap.bind(port).sync();
            log.info("[SERVER]服务端开启, 监听: " + port);
            f.channel().closeFuture().sync();
        } finally {
            mainGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
