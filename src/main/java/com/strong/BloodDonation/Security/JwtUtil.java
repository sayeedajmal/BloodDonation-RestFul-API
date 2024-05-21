package com.strong.BloodDonation.Security;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.strong.BloodDonation.Model.Staff;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateJwtToken(String email, String position) {
        return Jwts.builder()
                .issuer("BloodDonation")
                .subject("JWTToken")
                .claim("email", email)
                .claim("position", position)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 Hours
                .signWith(getSecretKey(), Jwts.SIG.HS512)
                .compact();
    }

    public String extractEmailFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }

    public Claims getClaim(String token) {
        return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(authToken);
            return true;
        } catch (Exception e) {
            System.out.println("Error validating JWT token: " + e.getMessage());
        }
        return false;
    }

    public String extractJwtToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        } else
            throw new JwtException("Authorization Header is Empty");
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Collection<? extends GrantedAuthority> getAuthorityList(Staff staff) {
        String authority = staff.getPosition().toString();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);
        return Collections.singletonList(grantedAuthority);
    }

}
