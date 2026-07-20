package com.url.shortner.security;

import com.url.shortner.service.UserDetailsImpl;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;
@Component
public class JwtUtils {
    // Authorization->Bearer <Token>
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration}")
    private Long jwtExpirationMs;
    public String getJwtFromHeader(HttpServletRequest request)
    {
        String BearerToken= request.getHeader("Authorization");
        if(BearerToken!=null && BearerToken.startsWith("Bearer "))
        {
            return BearerToken.substring(7);
        }
        return null;
    }

    public String getUsernameFromJwtToken(String token)
    {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    public String generateToken(UserDetailsImpl userDetails)
    {
        String username= userDetails.getUsername();
        String roles=userDetails.getAuthorities().stream()
                .map(authority->authority.getAuthority())
                .collect(Collectors.joining(","));
        Date now=new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .subject(username)
                .claim("roles",roles)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key())
                .compact();
    }

    private Key key()
    {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateToken(String authToken)
    {
        try {
            Jwts.parser().verifyWith((SecretKey) key())
                    .build().parseSignedClaims(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
