package com.compulynx.compas.security.jwt;

import com.compulynx.compas.security.model.Token;
import com.compulynx.compas.security.service.TokenRevocationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 60*60;

    //static final String SECRET = "4h1JfF1rkiwYjOzRzZXsBSKSPMZZn4DF77qqIaM9G0Yit3HT3xOJ5zCc5qRSi1n9yZoEGbBLHHq1v6r6HvWgGpq";
	static final String SECRET = "KADJAHDFKAEYAGHKSDYFAEYGSFDBAHSDHFYEAHFYAEJFSLYEHFAGHSYEFAHSBDFHAKYESFSAHFDGAESKYSGHFGAKFGHAKFAFDHGAKFSGHASGHFGAHhkaghfkafgafkaghfdgkfagfhagfhdsf32323347273727adkafhdfakfgKAGFAKDFGA";

    @Autowired
    private Environment env;
    @Autowired
    private TokenRevocationService tokenRevocationService;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        System.out.println("claims subject: "+claims.getSubject());

        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return false;
    }

    public Token generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, username);
    }

    private Token doGenerateToken(Map<String, Object> claims, String subject) {
        Token token = new Token();

        Date iat = new Date(System.currentTimeMillis());
        Date expiresAt = new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000);
        String compact = Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(iat)
                .setExpiration(expiresAt).signWith(SignatureAlgorithm.HS512, SECRET).compact();

        token.setAccess_token(compact);
        token.setIssued_at(iat);
        token.setExpires_in(expiresAt);

        return token;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && !tokenRevocationService.isTokenRevoked(token));
    }
}
