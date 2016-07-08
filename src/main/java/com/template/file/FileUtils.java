package com.template.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 一个对文件操作的工具类
 * Created by Cloud on 2016/6/27.
 */
public class FileUtils {
    /**
     * 格式化路径
     * 将反斜杠(\)替换成斜杠(/)
     * @param pathStr 原始路径
     * @return 格式化后的路径
     */
    public static String pathFormat(String pathStr) {
        //字符串为空则直接返回空路径
        if (null == pathStr) return "";
        char[] cs = pathStr.toCharArray();
        StringBuffer sb = new StringBuffer(256);
        for (char c : cs) {
            if (c == '\\')
                sb.append('/');
            else
                sb.append(c);
        }
        return sb.toString();
    }

    /**
     * 创建一个新文件
     * @param file 需要创建的文件
     * @throws IOException 抛出可能的异常
     */
    public static void createFile(File file) throws IOException {
        //若文件已经存在则直接返回
        if (file.exists()) return;
        //先创建文件夹
        new File(file.getParent()).mkdirs();
        file.createNewFile();
    }

    /**
     * 文件复制
     * @param from 被复制的文件
     * @param to 复制出来的新文件
     * @throws IOException 抛出IO异常
     */
    public static void fileCopy(File from, File to) throws IOException {
        if (!from.exists() || from.isDirectory())
            throw new IOException("文件不存在,或者只有文件夹!");
        //为防万一，先创建文件
        createFile(to);
        //文件复制的准备工作
        FileOutputStream fos = new FileOutputStream(to);
        FileInputStream fis  = new FileInputStream(from);
        FileChannel outputChannel = fos.getChannel();
        FileChannel inputChannel = fis.getChannel();
        //开始复制文件
        inputChannel.transferTo(0, inputChannel.size(), outputChannel);
        //善后工作
        inputChannel.close();
        outputChannel.close();
        fis.close();
        fos.close();
    }
}