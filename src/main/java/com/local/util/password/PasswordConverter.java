package com.local.util.password;

import jakarta.persistence.AttributeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PasswordConverter implements AttributeConverter<String, String> {
    private PasswordEncryptor passwordEncryptor;

    @Autowired
    public void setPasswordEncryptor(PasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return passwordEncryptor.hashPassword(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }
}
//FIXME: never use classes in JPA as it creates everything itself like a Neanderthal!
