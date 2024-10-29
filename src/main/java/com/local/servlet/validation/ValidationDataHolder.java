package com.local.servlet.validation;

public class ValidationDataHolder {
    private Class<?> objectClass;
    private String objectName;

    public ValidationDataHolder(Class<?> objectClass, String objectName) {
        this.objectClass = objectClass;
        this.objectName = objectName;
    }

    public Class<?> getObjectClass() {
        return objectClass;
    }

    public String getObjectName() {
        return objectName;
    }
}
