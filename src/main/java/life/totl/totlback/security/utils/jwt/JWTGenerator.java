package life.totl.totlback.security.utils.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import life.totl.totlback.security.utils.TotlSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTGenerator {

    @Autowired
    TotlSecurityProperties environment;

    public String generateToken(Authentication authentication) {
        String userName = authentication.getName();
        return Jwts.builder()
                .setSubject(userName)
                .claim("roles", authentication.getAuthorities().toString())
                .setIssuedAt(new Date())
                // set expiration date
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(environment.getJWT_SECRET()));
    }

    public String getUserNameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key()).build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException ex) {
            throw new AuthenticationCredentialsNotFoundException(ex.getMessage());
        }
    }
}
