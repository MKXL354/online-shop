//package com.local.dao.transaction;
//
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.locks.ReentrantLock;
//
//public class LockManager {
//    private ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();
//    private static final LockManager INSTANCE = new LockManager();
//
//    private LockManager() {}
//
//    public static LockManager getInstance() {
//        return INSTANCE;
//    }
//
//    public ReentrantLock getLock(String id) {
//        return locks.computeIfAbsent(id, k -> new ReentrantLock());
//    }
//}
