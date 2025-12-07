// package com.digidrive.document_service.security;

// import org.springframework.stereotype.Service;
// import io.jsonwebtoken.*;
// import io.jsonwebtoken.security.Keys;

// import java.security.Key;
// import java.util.Date;

// @Service
// public class JwtService {

//     private static final String SECRET = "thisisaverylongsecretkeythatmustbeatleast32characters";
//     private static final long EXPIRATION_MS = 86400000; // 1 day

//     private Key key() {
//         return Keys.hmacShaKeyFor(SECRET.getBytes());
//     }

//     public String generateToken(String email) {
//         return Jwts.builder()
//                 .setSubject(email)
//                 .setIssuedAt(new Date())
//                 .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
//                 .signWith(key(), SignatureAlgorithm.HS256)
//                 .compact();
//     }

//     public String extractEmail(String token) {
//         return Jwts.parserBuilder()
//                 .setSigningKey(key())
//                 .build()
//                 .parseClaimsJws(token)
//                 .getBody().getSubject();
//     }
// }

package com.digidrive.document_service.security;

import org.springframework.stereotype.Service;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    private static final String SECRET = "thisisaverylongsecretkeythatmustbeatleast32characters";
    private static final long EXPIRATION_MS = 86400000; // 1 day

    private Key key() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Generate token with roles
    public String generateToken(String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email)
                .addClaims(Map.of("roles", roles))  // Add roles claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract email from token
    public String extractEmail(String token) {
        return parseToken(token).getBody().getSubject();
    }

    // Extract roles from token
    public List<String> extractRoles(String token) {
        Object rolesObj = parseToken(token).getBody().get("roles");

        if (rolesObj instanceof List<?> list) {
            return list.stream().map(Object::toString).toList();
        }

        return List.of(); // fallback
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token);
    }
}
