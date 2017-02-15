package com.template.file;

import java.io.*;

/**
 * Created by Cloud on 2017-02-09.
 */
public class RandomAccessFileDom {
    /**
     * 测试RandomAccessFile类的读取方法
     *
     * @param path  文件地址
     * @param point 需要读取内容的指针位置
     */
    public static void randomRead(String path, int point) throws IOException {
        /* RandomAccessFile构造函数中，model参数详解
         * r   - 只读方式打开
         * rw  - 读写方式打开
         * rws - 读写方式打开，并对内容或元数据都同步写入底层存储设备
         * rwd - 读写方式打开，对文件内容的更新同步更新至底层存储设备
         */
        // 实例化RandomAccessFile类，并指明只读访问
        RandomAccessFile raf = new RandomAccessFile(path, "r");
        System.out.println("RandomAccessFile文件指针初始位置：" + raf.getFilePointer());
        // 移动至指定指针位置
        raf.seek(point);
        // 设置读取大小
        byte[] buff = new byte[1024];
        // 记录实际读取的字节数
        int hasRead = 0;
        // 循环读取
        while ((hasRead = raf.read(buff)) > 0) {
            System.out.println(new String(buff, 0, hasRead));
        }
    }

    /**
     * 测试RandomAccessFile类的写入方法，写在最末
     *
     * @param path 文件地址
     */
    public static void randomWriteToEnd(String path, String content) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(path, "rw");
        // 将指针移动到最末位置
        raf.seek(raf.length());
        raf.write(content.getBytes());
    }

    /**
     * 测试RandomAccessFile类的写入方法，在任意位置插入数据
     * @param path
     * @param points
     * @param content
     */
    public static void randomWrite(String path, long points, String content) throws IOException {
        /* 原理：
         * 由于直接向文件指定指针位置插入数据会覆盖原有的数据，
         * 所以需要利用临时文件暂存指定指针位置以后的全部数据，
         * 然后将需要插入的内容写入指定的指针位置，
         * 最后将临时文件中的内容重新写入原文件
         */
        // 创建一个临时文件，用于保存插入点后的数据
        File tmp = File.createTempFile("tmp", null);
        tmp.deleteOnExit();// JVM 虚拟机运行完毕后删除

        RandomAccessFile raf = new RandomAccessFile(path, "wr");
        FileOutputStream tmpOut = new FileOutputStream(tmp);
        FileInputStream tmpIn = new FileInputStream(tmp);
        raf.seek(points);

        byte[] buff = new byte[1024];
        int hasRead = 0;
        while ((hasRead = raf.read(buff)) > 0) {
            tmpOut.write(buff, 0, hasRead);
        }
        raf.seek(points);
        raf.write(content.getBytes());
        while ((hasRead = tmpIn.read()) > 0) {
            raf.write(buff, 0, hasRead);
        }
    }

    public static void main(String[] arg) {
        String path = "D:\\test.txt";
        try {
            RandomAccessFile raf = new RandomAccessFile(path, "rw");
            // 将指针移动到最末位置
            raf.seek(3);
            raf.write("这是测试的文字".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
