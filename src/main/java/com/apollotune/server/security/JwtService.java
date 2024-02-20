package com.apollotune.server.security;

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

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    // Token içinden kullanıcı adını çıkaran ana fonksiyon
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Token içinden belirli bir bilgiyi çıkaran generic fonksiyon
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // Token içindeki tüm talepleri çıkar
        final Claims claims = extractAllClaims(token);
        // Belirli bir talebi çözerek döndür
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Verilen bir token'ın geçerliliğini kontrol eden fonksiyon
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // Token'ın sahibi kullanıcı adı ile doğrulama ve token'ın süresinin dolup dolmadığını kontrol etme
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Verilen bir token'ın süresinin dolup dolmadığını kontrol eden fonksiyon
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Token içindeki tüm talepleri çıkaran fonksiyon
    private Claims extractAllClaims(String token) {
        // JSON Web Token (JWT) analizcisi oluştur
        // ve anahtarın doğruluğunu sağlamak için özel anahtarı kullan
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                // Token'ı çöz ve içindeki talepleri al
                .parseClaimsJws(token)
                .getBody();
    }

    // Özel anahtarın alındığı fonksiyon
    private Key getSignInKey() {
        // Base64 ile kodlanmış özel anahtarın byte dizisini çöz
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        // HMAC ile şifrelenmiş özel anahtar nesnesini oluştur ve döndür
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
