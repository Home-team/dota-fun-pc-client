package com.home.dotafun.protocol.file;

import com.home.dotafun.protocol.Protocol;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface FileProtocol extends Protocol {

    public String upload(String localPath, String remotePath) throws IOException;

    public String uploadBufferedImage(BufferedImage image, String remotePath) throws IOException;

    public String download(String remotePath, String localPath) throws IOException;
}
