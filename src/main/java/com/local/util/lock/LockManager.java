package com.local.util.lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Simple universal LockManager that provides lock for any resource and is accessed/used everywhere.
 * This helps coordination of locking between different services.
 */
public class LockManager {
    private ConcurrentHashMap<Object, ReentrantLock> locks = new ConcurrentHashMap<>();

    public ReentrantLock getLock(Object obj) {
        return locks.computeIfAbsent(obj, k -> new ReentrantLock());
    }
}
