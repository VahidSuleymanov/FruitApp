package com.example.FruitApp.service;

import com.example.FruitApp.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String SECRET_KEY;

    @Value("${security.jwt.access-expiration}")
    private long accessExpiration;

    @Value("${security.jwt.refresh-expiration}")
    private long refreshExpiration;

    public String findUsername(String token) {
        return exportToken(token, Claims::getSubject);
    }

    private <T> T exportToken(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsTFunction.apply(claims);
    }

    private Key getKey() {
        byte[] key = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }

    public boolean tokenControl(String jwt, UserDetails userDetails) {
        final String emailFromToken = findUsername(jwt);

        String emailFromUserDetails;
        if (userDetails instanceof User) {
            emailFromUserDetails = ((User) userDetails).getEmail();
        } else {
            emailFromUserDetails = userDetails.getUsername();
        }

        return (emailFromToken.equals(emailFromUserDetails)
                && !exportToken(jwt, Claims::getExpiration).before(new Date()));
    }

    public String generateAccessToken(UserDetails user) {
        return generateToken(user, false);
    }

    public String generateRefreshToken(UserDetails user) {
        return generateToken(user, true);
    }

    public String generateToken(UserDetails user, boolean isRefresh) {
        long expirationTime = isRefresh ? refreshExpiration : accessExpiration;
        String email = (user instanceof User) ? ((User) user).getEmail() : user.getUsername();
        String userId = (user instanceof User) ? ((User) user).getId().toString() : "";

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUserId(String token) {
        return exportToken(token, claims -> claims.get("userId", String.class));
    }

    public String generateTokenWithClaims(String email, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 300000)) // 5 dəqiqə
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return exportToken(token, Claims::getSubject);
    }

}