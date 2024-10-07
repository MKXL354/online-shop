package com.local.dao;

public interface PasswordEncryptor {
    String hashPassword(String password);
    boolean checkPassword(String password, String storedHash);
}
