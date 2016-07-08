package com.template.email;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * email 配置文件加载类
 * Created by Cloud on 2016/7/8.
 */
public class EmailProperty {
    private static Logger log = Logger.getLogger(EmailProperty.class);
    /**
     * 声明静态变量用于加载email配置文件
     */
    public static Properties props = new Properties();

    /**
     * 静态块，初始化时读取配置变量，用久保存
     */
    static {
        try {
            props.load(EmailProperty.class.getClassLoader().getResourceAsStream("email.properties"));
        } catch (IOException e) {
            log.error("加载email.properties时出错!!!", e);
        }
    }

    public static void main(String[] arg) {
        System.out.println(props.getProperty("smtp_server"));
        System.out.println(props.getProperty("imap_server"));
    }
}
