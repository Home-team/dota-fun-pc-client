package com.home.dotafun.util;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

public class Tools {

    public static String readFile(String fileName, String charsetName) {
        StringBuilder sb = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File(fileName), charsetName);
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine()).append(System.lineSeparator());
            }
            return sb.toString().trim();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return sb.toString();
    }

    public static Properties readPropertyFile(String fileName, String charsetName) {
        Properties property = null;
        try (InputStream fis = new FileInputStream(fileName); Reader reader = new InputStreamReader(fis, charsetName)) {
            property = new Properties();
            property.load(reader);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return property;
    }
}
