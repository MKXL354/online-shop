package com.local.service;

import com.local.commonexceptions.ApplicationRuntimeException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

public class JwtManager extends TokenManager {
    private Key secretKey;
    private long lifeTimeMillis;

    public JwtManager(String configFileLocation, long lifeTimeMillis) {
        super(configFileLocation);
        secretKey = readSecretKey();
        this.lifeTimeMillis = lifeTimeMillis;
    }

    private Key readSecretKey() {
        String secretKey = super.propertyManager.getProperty("secretKey");
        if (secretKey == null) {
            throw new ApplicationRuntimeException("bad config file format", null);
        }
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getSignedToken(Map<String, Object> claims){
        long startTimeMillis = System.currentTimeMillis();
        long endTimeMillis = startTimeMillis + lifeTimeMillis;
        JwtBuilder jwtBuilder = Jwts.builder();
        claims.forEach(jwtBuilder::claim);
        return jwtBuilder.issuedAt(new Date(startTimeMillis)).expiration(new Date(endTimeMillis)).signWith(secretKey).compact();
    }
}
