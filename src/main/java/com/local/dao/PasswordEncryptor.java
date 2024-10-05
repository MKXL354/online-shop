package com.local.dao;

import com.local.commonexceptions.ApplicationRuntimeException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PasswordEncryptor {
    private static final int ITERATIONS = 1000;
    private final static int SALT_LENGTH = 32;
    private static final int KEY_LENGTH = 256;

    private byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    public String hashPassword(String password) {
        byte[] salt = getSalt();
        return hashPassword(password, salt);
    }

    private String hashPassword(String password, byte[] salt) {
        try{
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        }
        catch(NoSuchAlgorithmException | InvalidKeySpecException e){
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    public boolean checkPassword(String password, String storedHash) {
        String[] saltAndHash = storedHash.split(":");
        String hashedPassword = hashPassword(password, Base64.getDecoder().decode(saltAndHash[0]));
        return hashedPassword.equals(storedHash);
    }
}
