package com.local.util;

import com.google.gson.Gson;
import com.local.exception.common.ApplicationRuntimeException;

import java.io.*;
import java.lang.ref.Cleaner;
import java.util.Map;

public class DiskPersistenceManager<K, V> {
    private static Cleaner cleaner = Cleaner.create();
    private Cleaner.Cleanable cleanable;
    private final static String SAVE_DIRECTORY = "E:\\IDEA Codes\\online-shop\\db\\mem";
    private String saveFileLocation;
    private Object resource;
    private Map<K, V> data;
    private Class<K> keyClass;
    private Class<V> valueClass;
    private Gson gson;

    public DiskPersistenceManager(Object resource, Map<K, V> data, Class<K> keyClass, Class<V> valueClass) {
        this.resource = resource;
        this.data = data;
        this.keyClass = keyClass;
        this.valueClass = valueClass;
        this.cleanable = cleaner.register(resource, this::persistData);
        this.saveFileLocation = SAVE_DIRECTORY + File.separator + resource.getClass().getSimpleName() + ".txt";
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
