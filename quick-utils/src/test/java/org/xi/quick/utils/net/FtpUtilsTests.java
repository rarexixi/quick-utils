package org.xi.quick.utils.net;

import org.apache.commons.net.ftp.*;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FtpUtilsTests {

    @Test
    public void listFilesTest() throws IOException {

        FtpUtils.init("192.168.1.63", 21, "ftp", "123456", false);

        // 循环遍历
        for (FTPFile ftpFile : FtpUtils.listFiles("/")) {
            System.out.println(ftpFile.getName());
        }
        FtpUtils.close();
    }

    @Test
    public void uploadTest() throws IOException {

        FtpUtils.init("192.168.1.63", 21, "ftp", "123456", false);

        File file = new File("/Users/xi/Pictures/5940c8e0959cb.jpg");
        try (FileInputStream inputStream = new FileInputStream(file)) {
            FtpUtils.upload("/pic/", file.getName(), inputStream);
        }
        FtpUtils.close();
    }

    @Test
    public void downloadTest() throws IOException {

        FtpUtils.init("192.168.1.63", 21, "ftp", "123456", false);

        // 循环遍历
        for (FTPFile ftpFile : FtpUtils.listFiles("/")) {
            if (ftpFile.isDirectory()) continue;
            File file = new File("/Users/xi/tmp/test/" + ftpFile.getName());
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                FtpUtils.download("/", file.getName(), outputStream);
                outputStream.flush();
            }
        }
        FtpUtils.close();
    }
}
