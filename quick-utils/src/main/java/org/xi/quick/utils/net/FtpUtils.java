package org.xi.quick.utils.net;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Function;

public class FtpUtils {

    String host;
    int port;
    String username;
    String password;
    boolean passiveMode;

    public FtpUtils(String host, String username, String password) throws IOException {
        this(host, 21, username, password, false);
    }

    public FtpUtils(String host, String username, String password, boolean passiveMode) throws IOException {
        this(host, 21, username, password, passiveMode);
    }

    public FtpUtils(String host, int port, String username, String password) throws IOException {
        this(host, port, username, password, false);
    }

    public FtpUtils(String host, int port, String username, String password, boolean passiveMode) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.passiveMode = passiveMode;
    }

    private FTPClient getFtpClient() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(host, port);
        ftpClient.login(username, password);

        // 设置上传为二进制文件
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        // 设置被动模式
        if (passiveMode) ftpClient.enterLocalPassiveMode();

        return ftpClient;
    }

    public void upload(String remoteDir, String fileName, InputStream inputStream) {
        execute(ftpClient -> {
            try {
                if (remoteDir.startsWith("/")) ftpClient.changeWorkingDirectory("/");

                for (String folder : remoteDir.split("/")) {
                    if (StringUtils.isEmpty(folder)) continue;
                    if (!ftpClient.changeWorkingDirectory(folder)) {
                        ftpClient.makeDirectory(folder);
                    }
                    ftpClient.changeWorkingDirectory(folder);
                }
                ftpClient.storeFile(fileName, inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public void download(String remoteDir, String fileName, OutputStream outputStream) {
        execute(ftpClient -> {
            try {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public FTPFile[] listFiles(String remoteDir) {
        return execute(ftpClient -> {
            try {
                return ftpClient.listFiles(remoteDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    /**
     * 关闭连接
     */
    public <T> T execute(Function<FTPClient, T> function) {
        FTPClient ftpClient = null;
        try {
            ftpClient = getFtpClient();
            if (function != null) {
                return function.apply(ftpClient);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient != null) {
                try {
                    if (ftpClient != null) {
                        ftpClient.logout();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (ftpClient.isConnected())
                            ftpClient.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
