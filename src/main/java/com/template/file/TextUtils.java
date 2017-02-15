package com.template.file;

import java.io.*;

/**
 * 操作文本文件的工具类
 * Created by Cloud on 2016/6/27.
 */
public class TextUtils {
    /**
     * 读取文本文件里的内容
     * @param txt
     * @param charset
     * @throws IOException
     */
    public static void readText(File txt, String charset) throws IOException {
        if (!txt.exists() || txt.isDirectory())
            throw new IOException("文件不存在,或只有文件夹!");
        FileInputStream fis = new FileInputStream(txt);
        InputStreamReader isr = new InputStreamReader(fis, charset);
        BufferedReader br = new BufferedReader(isr);
        String temp;
        while ((temp = br.readLine()) != null) {
            System.out.println("在此根据自己的需求添加各种功能--" + temp);
            // TODO: 2016/7/1 今后有具体需求的时候可以在这里添加功能
        }
        br.close();
        isr.close();
        fis.close();
    }

    /**
     * 往文件中写入内容
     * @param file 需要写入的文件
     * @param message 需要写入的内容
     * @param charset 内容的字符编码
     * @param isAppend 是否追加写入，true-追加写入、false-覆盖写入
     * @throws IOException
     */
    public static void writeText(File file, String message, String charset, boolean isAppend) throws IOException {
        if (!file.exists()) FileUtils.createFile(file);
        FileWriter fw = new FileWriter(file, isAppend);
//        PrintWriter pw = new PrintWriter(fw, true);// 第二个参数设定是否自动刷新缓冲区
        BufferedWriter bw = new BufferedWriter(fw);
        String msg = new String(message.getBytes(charset), charset);
        bw.write(msg);
//        pw.println(msg);
        bw.newLine();
        bw.flush();
        fw.flush();
//        pw.flush();// 如果构造函数中是否自动刷新缓冲区为TRUE，则此处无需再次刷新
        bw.close();
        fw.close();
//        pw.close();
    }
}
