package com.example.Backend.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.data.jpa.repository.query.KeysetScrollDelegate;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

@Service
public class JWTServices {
    private SecretKey secretKey;

    public JWTServices() throws NoSuchAlgorithmException {
        try {
            this.secretKey = KeyGenerator.getInstance("HmacSHA256").generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateToken(String username){
        Date _now = new Date();
        return Jwts
                .builder()
                .claims()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(_now.getTime() + 1000 * 60 * 60 * 24 * 7))
                .and()
                .signWith(this.secretKey)
                .compact();
    }

    public boolean validateToken(String token){
        try {
            Claims _tokenClaims = Jwts
                    .parser()
                    .verifyWith(this.secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            if (_tokenClaims.isEmpty())
                return false;
            if (_tokenClaims.getExpiration().before(new Date()))
                return false;
            if (_tokenClaims.getSubject() == null || _tokenClaims.getSubject().strip().isBlank())
                return false;
            return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public String extractJwtUsername(String token) {
        try {
            Claims _tokenClaims = Jwts
                    .parser()
                    .verifyWith(this.secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            if (_tokenClaims.isEmpty())
                return null;
            return _tokenClaims.getSubject();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

}
