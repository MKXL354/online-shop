package com.local.test.concurrency;

import com.local.util.lock.LockManager;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        LockManager lockManager = new LockManager();
        DataAccess dataAccess = new DataAccess(lockManager);
        try(ExecutorService executorService = Executors.newFixedThreadPool(4)){
            ArrayList<Callable<Object>> tasks = new ArrayList<>();
            Callable<Object> c1 = () -> {
                dataAccess.startTransaction();
                Datum d1 = dataAccess.getDatum(1);
                d1.setValue(d1.getValue() + 1);
                dataAccess.updateDatum(d1);

                Datum d2 = dataAccess.getDatum(2);
                d2.setValue(d2.getValue() + 1);
                dataAccess.updateDatum(d2);
                dataAccess.endTransaction();
                return null;
            };

            Callable<Object> c2 = () -> {
                dataAccess.startTransaction();
                Datum d2 = dataAccess.getDatum(2);
                d2.setValue(d2.getValue() + 1);
                dataAccess.updateDatum(d2);

                Datum d1 = dataAccess.getDatum(1);
                d1.setValue(d1.getValue() + 1);
                dataAccess.updateDatum(d1);
                dataAccess.endTransaction();
                return null;
            };

            for(int i = 0; i < 2; i++){
                tasks.add(c1);
            }
            for(int i = 0; i < 2; i++){
                tasks.add(c2);
            }

            executorService.invokeAll(tasks);
        }
    }
}
//FIXME: lock-order deadlock is solved by locking the entire collection.
