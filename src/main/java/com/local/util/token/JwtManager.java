package com.local.util.token;

import com.local.exception.common.ApplicationRuntimeException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtManager extends TokenManager {
    private SecretKey secretKey;
    private long lifeTimeMillis;

    public JwtManager(String configFileLocation, long lifeTimeMillis) {
        super(configFileLocation);
        secretKey = readSecretKey();
        this.lifeTimeMillis = lifeTimeMillis;
    }

    private SecretKey readSecretKey() {
        String secretKey = super.propertyManager.getProperty("secretKey");
        if (secretKey == null) {
            throw new ApplicationRuntimeException("bad config file format", null);
        }
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String getSignedToken(Map<String, Object> claims){
        long startTimeMillis = System.currentTimeMillis();
        long endTimeMillis = startTimeMillis + lifeTimeMillis;
        JwtBuilder jwtBuilder = Jwts.builder();
        claims.forEach(jwtBuilder::claim);
        return jwtBuilder.issuedAt(new Date(startTimeMillis)).expiration(new Date(endTimeMillis)).signWith(secretKey).compact();
    }

    public Map<String, Object> validateSignedToken(String jws, Map<String, Object> claims) throws InvalidTokenException, TokenExpiredException{
        try{
            JwtParserBuilder jwtParserBuilder = Jwts.parser();
            claims.forEach(jwtParserBuilder::require);
            return new HashMap<>(jwtParserBuilder.verifyWith(secretKey).build().parseSignedClaims(jws).getPayload());
        }
        catch(IllegalArgumentException e){
            throw new InvalidTokenException("empty token", e);
        }
        catch(ExpiredJwtException e){
            throw new TokenExpiredException("expired token", e);
        }
        catch(InvalidClaimException e){
            throw new InvalidTokenException("unauthorized claims", e);
        }
        catch(JwtException e){
            throw new InvalidTokenException("invalid token", e);
        }
    }
}
