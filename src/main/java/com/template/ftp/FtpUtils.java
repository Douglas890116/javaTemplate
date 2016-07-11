package com.template.ftp;

import com.template.file.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

/**
 * ftp 上传下载工具类
 * 采用Apache FTPClient类
 * Created by Cloud on 2016/6/28.
 */
public class FtpUtils {
    // FTPClient工具
    private static FTPClient ftpClient;
    // 登录信息
    private String serverIP;
    private int port;
    private String userName;
    private String password;
    // FTP相关参数配置
    private int fileTye = FTPClient.BINARY_FILE_TYPE; // 传输方式:二进制
    private int bufferSize = 1024 * 10; // 缓存大小
    private String charset = "UTF-8"; // 字符编码
    // ftp去掉头路径
    private static String whithoutHeadDir = "";

    /**
     * 构造函数，初始化登录信息
     *
     * @param serverIP ftp服务器IP地址
     * @param port     ftp夫妇器端口号
     * @param userName 登录用户名
     * @param password 登录密码
     */
    public FtpUtils(String serverIP, int port, String userName, String password) {
        this.serverIP = serverIP;
        this.port = port;
        this.userName = userName;
        this.password = password;
    }

    /**
     * 打开ftp连接
     * @return
     */
    public boolean open() throws IOException {
        if (null != ftpClient && ftpClient.isConnected()) return true;
        ftpClient = new FTPClient();
        // 连接FTP服务器
        ftpClient.connect(this.serverIP, this.port);
        ftpClient.login(this.userName, this.password);
        // 检测是否连接成功
        int reply = ftpClient.getReplyCode();
        if (FTPReply.isPositiveCompletion(reply)) {
            // 连接成功，设置基础配置
            ftpClient.enterLocalPassiveMode(); // 开通传输端口
            ftpClient.setFileType(this.fileTye); // 设置采用二进制传输
            ftpClient.setBufferSize(this.bufferSize); // 设置传输缓存大小
            ftpClient.setControlEncoding(this.charset); // 设置传输字符编码
            return true;
        } else {
            // 连接失败，关闭FTPClient
            this.close();
            return false;
        }
    }

    /**
     * 关闭FTP连接
     */
    public void close() throws IOException {
        if (null != ftpClient && ftpClient.isConnected())
            ftpClient.disconnect();
    }

    /**
     * 对路径中的字符进行编码格式化
     *
     * @param string
     * @return
     */
    public String UnicodeFormat(String string) throws UnsupportedEncodingException {
        return new String(string.getBytes("UTF-8"), "ISO-8859-1");
    }

    /**
     * 改变目录
     * @param ftpPath 目录地址
     * @return
     * @throws IOException
     */
    public boolean changeDir(String ftpPath) throws IOException {
        if (null == ftpClient || ! ftpClient.isConnected()) return false;
        ftpPath = FileUtils.pathFormat(ftpPath);
        ftpPath = UnicodeFormat(ftpPath);
        if (ftpPath.indexOf('/') < 0) {
            return ftpClient.changeWorkingDirectory(ftpPath);
        } else {
            String[] paths = ftpPath.split("/");
            boolean result = false;
            for (String path : paths) {
                result = ftpClient.changeWorkingDirectory(UnicodeFormat(path));
            }
            return  result;
        }
    }

    /**
     * 获取指定文件下下的的文件列表
     *
     * @param folderPath 文件夹位置
     * @return 返回文件列表
     */
    private FTPFile[] getFlieList(String folderPath) throws IOException {
        changeDir(folderPath);
        return ftpClient.listFiles();
    }

    /**
     * 创建目录
     * @param ftpPath 需要创建目录的路径
     * @return
     */
    public boolean mkDir(String ftpPath) throws IOException {
        if (!ftpClient.isConnected()) return false;
        // 格式化路径
        ftpPath = FileUtils.pathFormat(ftpPath);
        ftpPath = UnicodeFormat(ftpPath);
        if (ftpPath.indexOf('/') < 0) {
            // 只有一层目录
            ftpClient.makeDirectory(ftpPath);
            ftpClient.changeWorkingDirectory(ftpPath);
        } else {
            // 多层目录
            String[] paths = ftpPath.split("/");
            for (String path : paths) {
                ftpClient.makeDirectory(path);
                ftpClient.changeWorkingDirectory(UnicodeFormat(path));
            }
        }
        return true;
    }

    /**
     * 上传整个目录
     * @param file 需要上传到目录
     */
    public void upload(File file) throws IOException {
        if (file.isDirectory()) {
            ftpClient.makeDirectory(UnicodeFormat(file.getName()));
            ftpClient.changeWorkingDirectory(UnicodeFormat(file.getName()));
            String[] files = file.list();
            for (String str : files) {
                File _file = new File(file.getPath() + "/" + str);
                if (_file.isDirectory()) {
                    upload(_file);
                    ftpClient.changeToParentDirectory();
                } else {
                    File file_ = new File(file.getPath() + "/" + str);
                    FileInputStream input = new FileInputStream(file_);
                    ftpClient.storeFile(UnicodeFormat(file_.getName()), input);
                    input.close();
                }
            }
        } else {
            File file_ = new File(UnicodeFormat(file.getName()));
            FileInputStream input = new FileInputStream(file_);
            ftpClient.storeFile(UnicodeFormat(file_.getName()), input);
            input.close();
        }
    }

    /**
     * 將本地文件上上传到ftp服务器指定位置
     * @param localFile 本地文件位置
     * @param ftpFileName 上传到ftp服务器后的文件名
     * @param ftpDir 需要上传到ftp服务器的目录
     * @return
     */
    public boolean put(String localFile, String ftpFileName, String ftpDir) throws IOException {
        if (null == ftpClient || !ftpClient.isConnected()) return false;
        File srcFile = new File(localFile);
        FileInputStream fis = new FileInputStream(srcFile);
        mkDir(ftpDir);
        boolean isSuccess = ftpClient.storeFile(UnicodeFormat(ftpFileName), fis);
        IOUtils.closeQuietly(fis);
        return isSuccess;
    }

    /**
     * 从ftp服务器上下载文件到本地
     * @param ftpFile 服务器上的文件位置
     * @param localFile 下载到本地的文件位置
     * @return
     */
    public boolean get(String ftpFile, String localFile) throws IOException {
        if (null == ftpClient || !ftpClient.isConnected()) return false;
        ftpFile = FileUtils.pathFormat(ftpFile);
        String ftpDir = ftpFile.substring(0, ftpFile.lastIndexOf("/"));
        String ftpFileName = ftpFile.substring(ftpFile.lastIndexOf("/") + 1);
        this.changeDir(ftpDir);
        return ftpClient.retrieveFile(UnicodeFormat(ftpFileName), new FileOutputStream(localFile));
    }

    /**
     * 删除FTP上的文件
     * @param ftpFile
     * @return
     */
    public boolean deleteFile(String ftpFile) throws IOException {
        if (!ftpClient.isConnected()) return false;
        ftpClient.doCommandAsStrings("DELE", UnicodeFormat(ftpFile));
        return true;
    }

    /**
     * 产出ftp上的目录
     * @param dir
     * @return
     * @throws IOException
     */
    public boolean deleteDir(String dir) throws IOException {
        if (!ftpClient.isConnected()) return false;
        ftpClient.doCommandAsStrings("RMDA", UnicodeFormat(dir));
        return true;
    }

    /**
     * 删除整个目录
     * @param path
     * @return
     * @throws IOException
     */
    public boolean removeAll(String path) throws IOException {
        if (!ftpClient.isConnected()) return false;
        FTPFile[] files = ftpClient.listFiles(path);
        boolean result = true;
        for (FTPFile file : files) {
            if (file.isDirectory()) {
                result = removeAll(path + "/" + file.getName()) && result;
                result = ftpClient.removeDirectory(path) && result;
            } else {
                result = ftpClient.deleteFile(path + "/" + file.getName()) && result;
            }
        }
        return result;
    }

//////////////////////////////////////////////////一下方法需要进一步研究//////////////////////////////////////////////////
    /**
     * Download a whole directory from a FTP server.
     * @param ftpClient an instance of org.apache.commons.net.ftp.FTPClient class.
     * @param parentDir Path of the parent directory of the current directory being
     * downloaded.
     * @param currentDir Path of the current directory being downloaded.
     * @param saveDir path of directory where the whole remote directory will be
     * downloaded and saved.
     * @throws IOException if any network or IO error occurred.
     */
    /**
     * 单文件下载
     * 这个方法是找老外的文档 自己修改了很多，很不容易 包括中文 路径 乱码
     *@description 此方法描述的是：
     *@author  mf [海扬 QQ 929541303]
     *@version 2013-11-26下午10:13:07.
     */
//    public static boolean downloadSingleFile(String remoteFilePath, String savePath) throws IOException {
////		remoteFilePath = new String(remoteFilePath.getBytes("iso-8859-1"),"utf-8");
//        savePath = new String(savePath.getBytes("iso-8859-1"),"utf-8");
//        File downloadFile = new File(savePath);
//
//        File parentDir = downloadFile.getParentFile();
//        if (!parentDir.exists()) {
//            parentDir.mkdir();
//        }
//
//        OutputStream outputStream = new BufferedOutputStream(
//                new FileOutputStream(downloadFile));
//        try {
//            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//            return ftpClient.retrieveFile(remoteFilePath, outputStream);
//        } catch (IOException ex) {
//            throw ex;
//        } finally {
//            if (outputStream != null) {
//                outputStream.close();
//            }
//        }
//    }

    /**
     * Download a whole directory from a FTP server.
     * @param ftpClient an instance of org.apache.commons.net.ftp.FTPClient class.
     * @param parentDir Path of the parent directory of the current directory being
     * downloaded.
     * @param currentDir Path of the current directory being downloaded.
     * @param saveDir path of directory where the whole remote directory will be
     * downloaded and saved.
     * @throws IOException if any network or IO error occurred.
     */


    /**
     * 文件夹下载
     * 这个方法是找老外的文档 自己修改了很多，很不容易 包括中文 路径 乱码
     *@description 此方法描述的是：
     *@author  mf [海扬 QQ 929541303]
     *@version 2013-11-26下午10:13:07.
     */
//    public static void downloadDirectory(String parentDir, String currentDir, String saveDir) throws IOException {
//        String dirToList = parentDir;
//        if (!currentDir.equals("")) {
//            dirToList += "/" + currentDir;
//        }
//
//        FTPFile[] subFiles = ftpClient.listFiles(dirToList);
//
//        if (subFiles != null && subFiles.length > 0) {
//            for (FTPFile aFile : subFiles) {
//                String currentFileName = aFile.getName();
//                if (currentFileName.equals(".") || currentFileName.equals("..")) {
//                    // skip parent directory and the directory itself
//                    continue;
//                }
//                String filePath = parentDir + "/" + currentDir + "/"  + currentFileName;
//                if (currentDir.equals("")) {
//                    filePath = parentDir + "/" + currentFileName;
//                }
//
//                String newDirPath = saveDir + parentDir + File.separator + currentDir + File.separator + currentFileName;
//                if (currentDir.equals("")) {
//                    newDirPath = saveDir + parentDir + File.separator + currentFileName;
//                }
//                newDirPath = newDirPath.replaceAll(whithoutHeadDir, "");
//                if (aFile.isDirectory()) {
//                    // create the directory in saveDir
//                    File newDir = new File(new String(newDirPath.getBytes("iso-8859-1"),"utf-8"));
////	                File newDir = new File(newDirPath);// 这个
//                    boolean created = newDir.mkdirs();
//                    if (created) {
//                        System.out.println("下载文件夹: 【" + new String(newDirPath.getBytes("iso-8859-1"),"utf-8")+"】");
//                    } else {
//                        System.out.println("下载文件夹: 【" + new String(newDirPath.getBytes("iso-8859-1"),"utf-8")+"】");
//                    }
//
//                    // download the sub directory
//                    downloadDirectory(dirToList, currentFileName,saveDir);
//                } else {
//                    // download the file
//                    boolean success = downloadSingleFile(filePath,newDirPath);
//                    if (success) {
//                        System.out.println("成功  下载 " + new String(filePath.getBytes("iso-8859-1"),"utf-8"));
//                    } else {
//                        System.out.println("失败  中下载 " + new String(filePath.getBytes("iso-8859-1"),"utf-8"));
//                    }
//                }
//            }
//        }
//    }
}