package org.xi.quick.utils.net;

import org.apache.commons.net.ftp.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FtpUtils {

    private static FTPClient ftpClient = new FTPClient();

    public static void init(String host, int port, String username, String password, boolean passiveMode) throws IOException {

        ftpClient.connect(host, port);
        ftpClient.login(username, password);

        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        // 设置被动模式
        if (passiveMode) ftpClient.enterLocalPassiveMode();
    }

    public static void upload(String remoteDir, String fileName, InputStream inputStream) throws IOException {

        ftpClient.makeDirectory(remoteDir);
        ftpClient.changeWorkingDirectory(remoteDir);
        ftpClient.storeFile(fileName, inputStream);
    }

    public static void download(String remoteDir, String fileName, OutputStream outputStream) throws IOException {

        ftpClient.changeWorkingDirectory(remoteDir);
        // 得到FTP当前目录下所有文件
        FTPFile[] ftpFiles = ftpClient.listFiles();
        // 循环遍历
        for (FTPFile ftpFile : ftpFiles) {
            // 找到需要下载的文件
            if (ftpFile.getName().equals(fileName)) {
                ftpClient.retrieveFile(fileName, outputStream);
            }
        }
    }

    public static FTPFile[] listFiles(String remoteDir) throws IOException {

        FTPFile[] files = ftpClient.listFiles(remoteDir);
        return files;
    }

    public static void close() {
        try {
            if (ftpClient != null) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
