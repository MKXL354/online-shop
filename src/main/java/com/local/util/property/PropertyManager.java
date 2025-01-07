package com.local.util.property;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyManager {
    public static Properties loadProperties(String fileName) throws IOException {
        Properties properties = new Properties();
        try(FileInputStream fis = new FileInputStream(fileName)) {
            properties.load(fis);
        }
        return properties;
    }
}
