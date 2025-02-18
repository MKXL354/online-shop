package com.local.util.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwsTokenManager implements TokenManager {
    private String secretString;
    private SecretKey secretKey;
    private long lifeTimeMillis;

    @Autowired
    public void setSecretString(@Value("${token.secretKey}") String secretString) {
        this.secretString = secretString;
        setSecretKey(Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretString)));
    }

    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Autowired
    public void setLifeTimeMillis(@Value("${token.lifeTimeMillis}") long lifeTimeMillis) {
        this.lifeTimeMillis = lifeTimeMillis;
    }

    @Override
    public String getToken(Map<String, Object> claims){
        long startTimeMillis = System.currentTimeMillis();
        long endTimeMillis = startTimeMillis + lifeTimeMillis;
        JwtBuilder jwtBuilder = Jwts.builder();
        claims.forEach(jwtBuilder::claim);
        return jwtBuilder.issuedAt(new Date(startTimeMillis)).expiration(new Date(endTimeMillis)).signWith(secretKey).compact();
    }

    @Override
    public Map<String, Object> getClaims(String token) throws InvalidTokenException, TokenExpiredException{
        try{
            return new HashMap<>(Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload());
        }
        catch(IllegalArgumentException e){
            throw new InvalidTokenException("empty token", e);
        }
        catch(ExpiredJwtException e){
            throw new TokenExpiredException("expired token", e);
        }
        catch(JwtException e){
            throw new InvalidTokenException("invalid token", e);
        }
    }
}
