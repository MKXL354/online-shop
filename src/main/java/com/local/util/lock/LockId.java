package com.local.util.lock;

import java.util.Objects;

public class LockId {
    private final Class<?> objectClass;
    private final Object object;

    public LockId(Class<?> objectClass, Object object) {
        this.objectClass = objectClass;
        this.object = object;
    }

    public Class<?> getObjectClass() {
        return objectClass;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LockId lockId)) return false;
        return Objects.equals(objectClass, lockId.objectClass) && Objects.equals(object, lockId.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectClass, object);
    }
}