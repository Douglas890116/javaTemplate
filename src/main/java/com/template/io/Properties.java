package com.template.io;

import org.apache.log4j.Logger;

import java.io.IOException;

public class Properties {
    private static Logger log = Logger.getLogger(Properties.class);
    /**
     * 声明静态变量用于加载配置文件
     */
    public static java.util.Properties props = new java.util.Properties();
    public static String socket_server_ip;
    public static int socket_server_port;

    /**
     * 静态块，初始化时读取配置变量，用久保存
     */
    static {
        try {
            props.load(Properties.class.getClassLoader().getResourceAsStream("config.properties"));
            socket_server_ip = props.getProperty("socket_server_ip");
            socket_server_port = Integer.parseInt(props.getProperty("socket_server_port"));
        } catch (IOException e) {
            log.error("加载config.properties时出错!!!", e);
        }
    }
}
