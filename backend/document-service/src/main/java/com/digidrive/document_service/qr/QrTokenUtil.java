package com.digidrive.document_service.qr;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class QrTokenUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String vehicleNumber, Long userId) {

        long now = System.currentTimeMillis();
        long exp = now + (7 * 24 * 60 * 60 * 1000); // 7 days

        return Jwts.builder()
                .claim("vehicleNumber", vehicleNumber)
                .claim("userId", userId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(exp))
                .signWith(key)
                .compact();
    }

    public QrTokenData validate(String token) {
    var claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

    return new QrTokenData(
            claims.get("vehicleNumber", String.class),  // Read as String!
            claims.get("userId", Long.class)
    );
}


    
}
