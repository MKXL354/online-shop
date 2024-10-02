package com.local.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConfig {
    private String url;
    private String username;
    private String password;

    public DatabaseConfig(String configFileLocation) throws IOException {
        try(FileInputStream fis = new FileInputStream(configFileLocation)) {
            Properties property = new Properties();
            property.load(fis);
            this.url = property.getProperty("url");
            this.username = property.getProperty("username");
            this.password = property.getProperty("password");
            if(url == null || username == null || password == null){
                throw new DatabaseConfigException("bad config file format", null);
            }
        }
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
