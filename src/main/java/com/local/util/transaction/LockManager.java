package com.local.util.transaction;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class LockManager {
    private ConcurrentHashMap<LockId, ReentrantLock> locks = new ConcurrentHashMap<>();
    private static final LockManager INSTANCE = new LockManager();

    private LockManager() {}

    public static LockManager getInstance() {
        return INSTANCE;
    }

    public ReentrantLock getLock(Class<?> objectClass, Object id) {
        LockId lockId = new LockId(objectClass, id);
        return locks.computeIfAbsent(lockId, k -> new ReentrantLock());
    }
}
