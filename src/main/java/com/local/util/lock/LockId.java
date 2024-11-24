package com.local.util.lock;

import java.util.Objects;

public class LockId {
    private final Class<?> objectClass;
    private final Object id;

    public LockId(Class<?> objectClass, Object id) {
        this.objectClass = objectClass;
        this.id = id;
    }

    public Class<?> getObjectClass() {
        return objectClass;
    }

    public Object getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LockId lockId)) return false;
        return Objects.equals(objectClass, lockId.objectClass) && Objects.equals(id, lockId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectClass, id);
    }
}
