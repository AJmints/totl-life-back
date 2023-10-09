package life.totl.totlback.security.utils.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import life.totl.totlback.security.utils.TotlSecurityProperties;
import life.totl.totlback.users.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Component
public class JWTGenerator {

    @Autowired
    TotlSecurityProperties environment;
    @Autowired
    UserEntityRepository userEntityRepository;

    public String generateToken(Authentication authentication) {
        String userName = authentication.getName();

        return Jwts.builder()
                .setSubject(userEntityRepository.findByUserName(userName).getIdString())
                .claim("roles", authentication.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(calculateExpiryDate())
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, 360); // 6 hours
        return new Date(cal.getTime().getTime());
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
