package com.template.email;

import jodd.format.Printf;
import jodd.mail.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static com.template.email.EmailProperty.props;
/**
 * 收邮件工具类
 * 采用Jodd Email组件：http://jodd.org/doc/email.html#receiving-emails
 * Created by Cloud on 2016/7/8.
 */
public class ReceiveEmailUtils {
    private String IMAP_SERVER; // IMAP服务器地址
    private int IMAP_SSL_PORT;  // SSL端口号
    private String IMAP_ADDRESS;// 邮箱地址
    private String PASSWORD;    // 邮箱密码

    private ImapServer imapServer;
//    private Pop3Server pop3Server; // 使用POP收邮件与IMAP类似，留下来，但注释掉方便今后查看

    /**
     * 无参构造函数
     * 收取默认邮箱邮件
     */
    public ReceiveEmailUtils() {
        this.IMAP_SERVER = props.getProperty("imap_server");
        this.IMAP_SSL_PORT = Integer.parseInt(props.getProperty("imap_ssl_port"));
        this.IMAP_ADDRESS = props.getProperty("imap_address");
        this.PASSWORD = props.getProperty("imap_password");
        imapServer = new ImapSslServer(this.IMAP_SERVER, this.IMAP_SSL_PORT, this.IMAP_ADDRESS, this.PASSWORD);
//        pop3Server = new Pop3SslServer(this.IMAP_SERVER, this.IMAP_SSL_PORT, this.IMAP_ADDRESS, this.PASSWORD);
    }


    /**
     * 收取指定邮箱的邮件
     * @param email_address
     * @param password
     * @param imap_server
     * @param imap_port
     */
    public ReceiveEmailUtils(String email_address, String password, String imap_server, int imap_port) {
        this.IMAP_SERVER = imap_server;
        this.IMAP_SSL_PORT = imap_port;
        this.IMAP_ADDRESS = email_address;
        this.PASSWORD = password;
        imapServer = new ImapSslServer(this.IMAP_SERVER, this.IMAP_SSL_PORT, this.IMAP_ADDRESS, this.PASSWORD);
//        pop3Server = new Pop3SslServer(this.IMAP_SERVER, this.IMAP_SSL_PORT, this.IMAP_ADDRESS, this.PASSWORD);
    }

    /**
     * 获取邮件
     * @return
     */
    public ReceivedEmail[] receivedEmails() {
        ReceiveMailSession session = imapServer.createSession();
//        ReceiveMailSession session = pop3Server.createSession();
        session.open();
        System.out.println("getMessageCount:\t\t" + session.getMessageCount());
        System.out.println("getNewMessageCount:\t\t" + session.getNewMessageCount());
        System.out.println("getUnreadMessageCount:\t\t" + session.getUnreadMessageCount());
        System.out.println("getDeletedMessageCount:\t\t" + session.getDeletedMessageCount());
        System.out.println("==================================================");
        ReceivedEmail[] emails = session.receiveEmailAndMarkSeen();
        System.out.println("receiveEmail：\t\t" + emails.length);
//        ReceivedEmail[] emails = session.receiveEmail(); // 获取所有未删除的邮件邮件
//        ReceivedEmail[] emails = session.receiveEmailAndMarkSeen(); // 获取邮件 并 标记为已读
//        ReceivedEmail[] emails = session.receiveEmailAndDelete(); // 获取邮件 并 删除
        session.close();
        return emails;
    }

    /**
     * 读取邮件
     * @param email
     */
    public void readEmail(ReceivedEmail email) {
        if (null == email) return;
        System.out.println("\n\n===[" + email.getMessageNumber() + "]===");

        // common info
//        Printf.str("%0x", email.getFlags());
        System.out.println("FROM:" + email.getFrom());
        System.out.println("TO:" + email.getTo()[0]);
        System.out.println("SUBJECT:" + email.getSubject());
        System.out.println("PRIORITY:" + email.getPriority());
        System.out.println("SENT DATE:" + email.getSentDate());
        System.out.println("RECEIVED DATE: " + email.getReceiveDate());

        // process messages
        List<EmailMessage> messages = email.getAllMessages();
        for (EmailMessage msg : messages) {
            System.out.println("------");
            System.out.println(msg.getEncoding());
            System.out.println(msg.getMimeType());
            System.out.println(msg.getContent());
        }

        // process attachments
        List<EmailAttachment> attachments = email.getAttachments();
        if (attachments != null) {
            System.out.println("+++++");
            for (EmailAttachment attachment : attachments) {
                System.out.println("name: " + attachment.getName());
                System.out.println("cid: " + attachment.getContentId());
                System.out.println("size: " + attachment.getSize());
                attachment.writeToFile(
                        new File("d:\\", attachment.getName()));
            }
        }
    }
    public static void main(String[] arg) {
        ReceiveEmailUtils receive = new ReceiveEmailUtils();
        List<ReceivedEmail> emails = Arrays.asList(receive.receivedEmails());
        if (null != emails && emails.size() > 0) {
            for (ReceivedEmail email : emails) {
                receive.readEmail(email);
            }
        }
    }
}