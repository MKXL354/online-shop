package com.local.util.lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class LockManager {
    private ConcurrentHashMap<LockId, ReentrantLock> locks = new ConcurrentHashMap<>();

    public ReentrantLock getLock(Class<?> objectClass, Object id) {
        LockId lockId = new LockId(objectClass, id);
        return locks.computeIfAbsent(lockId, k -> new ReentrantLock());
    }
}
