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
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String SECRET_KEY;

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

    public String generateToken(UserDetails user) {
        String email;
        if (user instanceof User) {
            email = ((User) user).getEmail();
        } else {
            email = user.getUsername();
        }

        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}