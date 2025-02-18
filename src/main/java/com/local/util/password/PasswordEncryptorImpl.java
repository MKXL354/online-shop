package com.local.util.password;

import com.local.exception.common.ApplicationRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
public class PasswordEncryptorImpl implements PasswordEncryptor {
    private int iterations;
    private int saltLength;
    private int keyLength;

    @Autowired
    public void setIterations(@Value("${encryptor.iterations}") int iterations) {
        this.iterations = iterations;
    }

    @Autowired
    public void setSaltLength(@Value("${encryptor.saltLength}") int saltLength) {
        this.saltLength = saltLength;
    }

    @Autowired
    public void setKeyLength(@Value("${encryptor.keyLength}") int keyLength) {
        this.keyLength = keyLength;
    }

    private byte[] getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[saltLength];
        random.nextBytes(salt);
        return salt;
    }

    private String hashPassword(String password, byte[] salt) {
        try{
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        }
        catch(NoSuchAlgorithmException | InvalidKeySpecException e){
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String hashPassword(String password) {
        byte[] salt = getSalt();
        return hashPassword(password, salt);
    }

    @Override
    public boolean checkPassword(String password, String storedHash) {
        String[] saltAndHash = storedHash.split(":");
        String hashedPassword = hashPassword(password, Base64.getDecoder().decode(saltAndHash[0]));
        return hashedPassword.equals(storedHash);
    }
}
