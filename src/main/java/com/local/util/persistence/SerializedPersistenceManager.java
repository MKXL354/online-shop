package com.local.util.persistence;

import com.local.exception.common.ApplicationRuntimeException;

import java.io.*;

public class SerializedPersistenceManager implements PersistenceManager{
    private final static String SAVE_DIRECTORY = "E:\\IDEA Codes\\online-shop\\db\\mem";

    @Override
    public void persistData(Object object){
        String fileName = SAVE_DIRECTORY + File.separator + object.getClass().getSimpleName() + ".txt";
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))){
            oos.writeObject(object);
        }
        catch (IOException e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public <T> T loadData(Class<T> type) {
        String fileName = SAVE_DIRECTORY + File.separator + type.getSimpleName() + ".txt";
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))){
            return (type.cast(ois.readObject()));
        }
        catch(IOException | ClassNotFoundException | ClassCastException e) {
            return null;
        }
    }
}