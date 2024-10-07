package com.local.util;

import com.local.commonexceptions.ApplicationRuntimeException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyManager {
    private Map<String, String> propertiesMap;

    public PropertyManager(String propertyFileLocation) {
        propertiesMap = new HashMap<>();
        try(FileInputStream fileInputStream = new FileInputStream(propertyFileLocation)){
            Properties properties = new Properties();
            properties.load(fileInputStream);
            properties.forEach((key, value) -> propertiesMap.put(key.toString(), value.toString()));
        }
        catch(IOException e){
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    public String getProperty(String key) {
        return propertiesMap.get(key);
    }
}
