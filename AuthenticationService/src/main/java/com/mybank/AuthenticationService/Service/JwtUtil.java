package com.mybank.AuthenticationService.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {

   
    private final String SECRET = "hm1kq+MhgyH1WJ+pCPOyxrPMc/0WNeJOIXjwu09JliX7h3Fkni0aRzRrLFXlyZi/qPC23QJ2bL34XxXaQaDEG1lW67mTcEjsQZI4xOeTxdgyUmltQYy/Q2e6FvyiyvAO9VxlaDdGkAlRQOJdPZU3u7wXTaS+pu5Bv15/STaOGo9H2hcilAXh3ulFo4WhxuJ3p4YwGJIZ0+yneue/uQDRwCYNamIyUjGTaSbD5j3KWIPieWP4yM2KGOGQe6W/vXSeEOghy6RLc5zeKpvXHC2Y2IXI+oUgrxMyZFtjPWQX1obcCMrTHLiMfk0eaISUEa4sPM6OfGvkitS+8XcuIO85dLCZ45rJYmWkBQe3kdicDbM=";

    // Get the username from the token
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    // Get the expiration date from the token
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    // Extract claims from the token
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if the token is expired
    public boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    // Generate a new token with the given username
    public String generateToken(String userName, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        return createToken(claims, userName);
    }

    // Create a token with specified claims and username
    public String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) 
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Generate the signing key from the secret
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Validate the token against the user details
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String userName = getUsernameFromToken(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
}
