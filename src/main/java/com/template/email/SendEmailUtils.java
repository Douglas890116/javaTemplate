package com.template.email;

import jodd.mail.Email;
import jodd.mail.SendMailSession;
import jodd.mail.SmtpServer;
import jodd.mail.SmtpSslServer;

import static com.template.email.EmailProperty.props;

/**
 * 发邮件工具类
 * 采用Jodd Email组件：http://jodd.org/doc/email.html
 * Created by Cloud on 206/7/8.
 */
public class SendEmailUtils {

    private String SMTP_SERVER; // smtp服务器地址
    private int    SMTP_SSL_PORT;  // ssl端口号
    private String SMTP_ADDRESS;// 邮箱地址
    private String PASSWORD;    // 邮箱密码

    private SmtpServer smtpServer;//全局变量

    /**
     * 无参构造函数，用默认账号发送
     */
    public SendEmailUtils() {
        this.SMTP_SERVER = props.getProperty("smtp_server");
        this.SMTP_SSL_PORT = Integer.parseInt(props.getProperty("smtp_ssl_port"));
        this.SMTP_ADDRESS = props.getProperty("smtp_address");
        this.PASSWORD = props.getProperty("smtp_password");
        smtpServer = SmtpSslServer
                .create(this.SMTP_SERVER, SMTP_SSL_PORT)
                .authenticateWith(this.SMTP_ADDRESS, this.PASSWORD);
    }

    /**
     * 有参构造函数
     * 用自己的邮件服务器发送
     *
     * @param from_address 发送邮箱
     * @param password     密码
     * @param smtp_server  smtp服务器地址
     * @param ssl_port     SSL端口
     */
    public SendEmailUtils(String from_address, String password, String smtp_server, int ssl_port) {
        this.SMTP_ADDRESS = from_address;
        this.PASSWORD = password;
        this.SMTP_SERVER = smtp_server;
        this.SMTP_SSL_PORT = ssl_port;
        smtpServer = SmtpSslServer
                .create(this.SMTP_SERVER, SMTP_SSL_PORT)
                .authenticateWith(this.SMTP_ADDRESS, this.PASSWORD);
    }

    /**
     * 发送html格式的邮件
     *
     * @param to_address    收件箱地址
     * @param title         邮件标题
     * @param content_html  html格式的邮件内容
     */
    public void sendHtmlEmail(String to_address, String title, String content_html) {
        Email emailHtml = Email.create()
                .from(this.SMTP_ADDRESS)
                .to(to_address)
                .subject(title, "UTF-8")
                .addHtml(content_html, "UTF-8");
        SendMailSession session = smtpServer.createSession();
        session.open();
        session.sendMail(emailHtml);
        session.close();
    }

    /**
     * 发送文本格式的邮件
     *
     * @param to_address    收件箱地址
     * @param title         邮件标题
     * @param content_text  文本格式的邮件内容
     */
    public void sendTextEmail(String to_address, String title, String content_text) {
        Email emailText = Email.create()
                .from(this.SMTP_ADDRESS)
                .to(to_address)
                .subject(title, "UTF-8")
                .addText(content_text, "UTF-8");
        SendMailSession session = smtpServer.createSession();
        session.open();
        session.sendMail(emailText);
        session.close();
    }

    public static void main(String[] arg) {
        SendEmailUtils send = new SendEmailUtils();
        send.sendHtmlEmail("cloud@networkws.com", "测试邮件", "<h6>你好</h6>");
    }
}