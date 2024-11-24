package com.local.util.persistence;

import com.google.gson.Gson;
import com.local.exception.common.ApplicationRuntimeException;

import java.io.*;
import java.lang.ref.Cleaner;
import java.util.Map;

public class DiskPersistenceManager<K, V> {
    private static Cleaner cleaner = Cleaner.create();
    private final static String SAVE_DIRECTORY = "E:\\IDEA Codes\\online-shop\\db\\mem\\test";
    private String saveFileLocation;
    private Map<K, V> data;
    private Class<K> keyClass;
    private Class<V> valueClass;
    private Gson gson;

    public DiskPersistenceManager(String className, Map<K, V> data, Class<K> keyClass, Class<V> valueClass) {
        this.data = data;
        this.keyClass = keyClass;
        this.valueClass = valueClass;
        this.saveFileLocation = SAVE_DIRECTORY + File.separator + className + ".txt";
        this.gson = new Gson();
    }

    public void loadData(){
        try(BufferedReader reader = new BufferedReader(new FileReader(saveFileLocation))){
            String line;
            while((line = reader.readLine()) != null){
                String[] split = line.split(":", 2);
                K key = gson.fromJson(split[0], keyClass);
                V value = gson.fromJson(split[1], valueClass);
                data.put(key, value);
            }
        }
        catch (IOException e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    public void persistData(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(saveFileLocation))){
            for(Map.Entry<K, V> entry : data.entrySet()){
                writer.write(gson.toJson(entry.getKey()) + ":" + gson.toJson(entry.getValue()));
                writer.newLine();
            }
        }
        catch (IOException e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }
}
