package com.local.util.password;

import jakarta.persistence.AttributeConverter;

public class PasswordConverter implements AttributeConverter<String, String> {
    private PasswordEncryptor passwordEncryptor;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return passwordEncryptor.hashPassword(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }
}
