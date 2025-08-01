package uz.jvh.nextpizza.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {
    private static final String secretKey = "753778214125442A472D4B6150645367566B59703373367639792F423F452848";

    public String generateToken(@NonNull String username){
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setIssuer("http://localhost:8080/uz.pro.usm")
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 10))
                .signWith(signKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key signKey(){
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    public boolean isValid(@NonNull String token) {
        try {
            Claims claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getUsername(@NonNull String token){
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    private Claims getClaims(String token){
        return Jwts.parser()
                .setSigningKey(signKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
