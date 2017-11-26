package com.template.io.bio;

import com.template.common.Calculator;
import org.apache.log4j.Logger;

import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerHandler implements Runnable {
    private static Logger log = Logger.getLogger(ServerHandler.class);

    private Socket socket;

    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        BufferedReader reader = null;
        PrintWriter writer = null;
        log.debug("准备操作客户端信息...");
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            String message;
            String result;

            while (true) {
                // 通过BufferedReader读取一行
                // 如果已经读到输入流尾部，返回null并退出循环
                // 如果得到非空值，就尝试计算结果并返回
                if ((message = reader.readLine()) == null) break;

                log.info("服务端收到信息: " + message);

//                try {
//                    result = Calculator.Instance.cal(message).toString();
//                } catch (Exception e) {
//                    result = "计算错误: " + e.getMessage();
//                }

                result = "["+dateFormat.format(new Date()) + "]"+message;
                writer.println(result);
            }
        } catch (IOException e) {
            log.error("run() 方法错误!", e);
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }
}
