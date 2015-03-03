package com.home.dotafun.protocol.file.impl;

import com.home.dotafun.protocol.file.FileProtocol;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class FtpFileProtocol implements FileProtocol {
    private String server;
    private int port;
    private String user;
    private String password;
    private FTPClient ftpClient;

    public FtpFileProtocol() {
        server = null;
        port = -1;
        user = null;
        password = null;
        ftpClient = null;
    }

    public void connect(String server, int port) throws IOException {
        this.server = server;
        this.port = port;
        ftpClient = new FTPClient();
        ftpClient.connect(this.server, this.port);
    }

    public void login(String user, String password) throws IOException {
        this.user = user;
        this.password = password;
        ftpClient.login(this.user, this.password);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    }

    @Override
    public String upload(String localPath, String remotePath) throws IOException {
        InputStream inputStream = null;
        String result = null;
        boolean isDone = false;
        File localFile = new File(localPath.trim());
        inputStream = new FileInputStream(localFile);
        isDone = ftpClient.storeFile(remotePath.trim(), inputStream);
        inputStream.close();
        if (isDone) {
            StringBuffer link = new StringBuffer();
            link.append("ftpFileProtocol://").append(user).append(":").append(password).append("@").append(server).append("/").append(remotePath);
            result = link.toString().replace(" ", "%20");
        }
        return result;
    }

    @Override
    public String uploadBufferedImage(BufferedImage image, String remotePath) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String result = null;
        boolean isDone = false;
        ImageIO.write(image, "jpg", outputStream);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        isDone = ftpClient.storeFile(remotePath, inputStream);
        if (isDone) {
            StringBuffer link = new StringBuffer();
            link.append("ftpFileProtocol://").append(user).append(":").append(password).append("@").append(server).append("/").append(remotePath);
            result = link.toString().replace(" ", "%20");
        }
        return result;
    }

    @Override
    public String download(String remotePath, String localPath) throws IOException {
        return null;
    }

    public boolean makeDirectory(String path) throws IOException {
        return ftpClient.makeDirectory(path);
    }

    public String getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FtpFileProtocol)) return false;

        FtpFileProtocol ftpFileProtocol = (FtpFileProtocol) o;

        if (port != ftpFileProtocol.port) return false;
        if (password != null ? !password.equals(ftpFileProtocol.password) : ftpFileProtocol.password != null) return false;
        if (server != null ? !server.equals(ftpFileProtocol.server) : ftpFileProtocol.server != null) return false;
        if (user != null ? !user.equals(ftpFileProtocol.user) : ftpFileProtocol.user != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = server != null ? server.hashCode() : 0;
        result = 31 * result + port;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FtpFileProtocol{");
        sb.append("password='").append(password).append('\'');
        sb.append(", user='").append(user).append('\'');
        sb.append(", port=").append(port);
        sb.append(", server='").append(server).append('\'');
        sb.append('}');
        return sb.toString();
    }
}