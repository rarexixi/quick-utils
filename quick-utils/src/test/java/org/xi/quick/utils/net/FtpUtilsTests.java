package org.xi.quick.utils.net;

import org.apache.commons.net.ftp.*;
import org.junit.Test;

import java.io.*;

public class FtpUtilsTests {

    @Test
    public void listFilesTest() throws IOException {

        FtpUtils ftpUtils = new FtpUtils("192.168.1.63", 21, "ftp", "123456");

        // 循环遍历
        for (FTPFile ftpFile : ftpUtils.listFiles("/")) {
            System.out.println(ftpFile.getName());
        }
    }

    @Test
    public void uploadTest() throws IOException {

        FtpUtils ftpUtils = new FtpUtils("192.168.1.63", 21, "ftp", "123456");

        File file = new File("/Users/xi/Pictures/5940c8e0959cb.jpg");
        try (FileInputStream inputStream = new FileInputStream(file)) {
            ftpUtils.upload("/bjev/201808/16/", file.getName(), inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        listFilesTest();
    }

    @Test
    public void downloadTest() throws IOException {

        FtpUtils ftpUtils = new FtpUtils("192.168.1.63", 21, "ftp", "123456");

        // 循环遍历
        for (FTPFile ftpFile : ftpUtils.listFiles("/")) {
            if (ftpFile.isDirectory()) continue;
            File file = new File("/Users/xi/tmp/test/" + ftpFile.getName());
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                ftpUtils.download("/", file.getName(), outputStream);
                outputStream.flush();
            }
        }
    }
}
