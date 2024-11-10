package com.local.util.lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Simple universal LockManager that provides lock for any resource and is accessed/used everywhere.
 * This helps coordination of locking between different services.
 */
public class LockManager {
    private ConcurrentHashMap<LockId, ReentrantLock> locks = new ConcurrentHashMap<>();

    public ReentrantLock getLock(Class<?> objectClass, Object obj) {
        LockId lockId = new LockId(objectClass, obj);
        return locks.computeIfAbsent(lockId, k -> new ReentrantLock());
    }
}
