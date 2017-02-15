package com.template.file;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * 一个对文件操作的工具类
 * Created by Cloud on 2016/6/27.
 */
public class FileUtils {
    /**
     * 格式化路径
     * 将反斜杠(\)替换成斜杠(/)
     *
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
     *
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
     * 通过单个字节复制文件，效率极低!!!
     *
     * @param from 源文件
     * @param to   目标文件
     */
    public static void fileCopyByByte(File from, File to) throws IOException {
        if (!from.exists() || !from.isFile())
            throw new IOException("源文件不存在，或者只是文件夹!");
        FileInputStream fis = new FileInputStream(from);
        FileOutputStream fos = new FileOutputStream(to);
        int size;
        while ((size = fis.read()) >= 0) {
            fos.write(size);
            fos.flush();
        }
        fis.close();
        fos.close();
    }

    /**
     * 通过字节数组复制文件
     *
     * @param from 源文件
     * @param to   目标文件
     * @throws IOException
     */
    public static void fileCopyByBytes(File from, File to) throws IOException {
        if (!from.exists() || !from.isFile())
            throw new IOException("源文件不存在，或者只是文件夹");
        FileInputStream fis = new FileInputStream(from);
        FileOutputStream fos = new FileOutputStream(to);
        byte[] buf = new byte[1000 * 1024];
        int size;
        while ((size = fis.read(buf, 0, buf.length)) >= 0) {
            fos.write(buf, 0, size);
            fos.flush();
        }
        fis.close();
        fos.close();
    }

    /**
     * 通过带缓冲的单字节复制文件，效率极低!!!
     *
     * @param from 源文件
     * @param to   目标文件
     * @throws IOException
     */
    public static void fileCopyByBufferedByte(File from, File to) throws IOException {
        if (!from.exists() || !from.isFile())
            throw new IOException("源文件不存在，或者只是文件夹!");
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(from));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(to));
        int size;
        while ((size = bis.read()) >= 0) {
            bos.write(size);
            bos.flush();
        }
        bis.close();
        bos.close();
    }

    /**
     * 通过带缓冲的字节数组复制文件
     *
     * @param from 源文件
     * @param to   目标文件
     * @throws IOException
     */
    public static void fileCopyByBufferedBytes(File from, File to) throws IOException {
        if (!from.exists() || !from.isFile())
            throw new IOException("源文件不存在，或者只是文件夹!");
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(from));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(to));
        byte[] buff = new byte[1000 * 1024];
        int size;
        while ((size = bis.read(buff, 0, buff.length)) >= 0) {
            bos.write(buff, 0, size);
            bos.flush();
        }
        bis.close();
        bos.close();
    }

    /**
     * 通过通道复制文件
     *
     * @param from 源文件
     * @param to   目标文件
     * @throws IOException 抛出IO异常
     */
    public static void fileCopyByChannel(File from, File to) throws IOException {
        if (!from.exists() || !from.isFile())
            throw new IOException("文件不存在,或者只有文件夹!");
        //文件复制的准备工作
        FileOutputStream fos = new FileOutputStream(to);
        FileInputStream fis = new FileInputStream(from);
        FileChannel outputChannel = fos.getChannel();
        FileChannel inputChannel = fis.getChannel();
        //开始复制文件
//        inputChannel.transferTo(0, inputChannel.size(), outputChannel);
        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        //善后工作
        inputChannel.close();
        outputChannel.close();
        fis.close();
        fos.close();
    }

    public static void main(String[] arg) {
    }
}