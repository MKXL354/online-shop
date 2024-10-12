package com.local.util.password;

public interface PasswordEncryptor {
    String hashPassword(String password);
    boolean checkPassword(String password, String storedHash);
}
