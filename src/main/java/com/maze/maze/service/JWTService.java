package com.maze.maze.service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";
	public String extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
	private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
	private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
	public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
	private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
	public Boolean validateToken(String token, UserDetails userDetails) {
        final String phoneNumber = extractPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
	public String GenerateToken(String phoneNumber, Map<String, Object> map){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, phoneNumber, map);
    }



    private String createToken(Map<String, Object> claims, String phoneNumber, Map<String, Object> map) {

        return Jwts.builder()
                .setClaims(claims)
                .setClaims(map)
                .setSubject(phoneNumber)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }
}
