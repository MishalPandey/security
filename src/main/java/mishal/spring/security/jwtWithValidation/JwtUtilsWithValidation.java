package mishal.spring.security.jwtWithValidation;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component

public class JwtUtilsWithValidation {

    private String jwtSecret = "YS1zdHJpbmctc2VjcmV0LWF0LWxlYXN0LTI1Ni1iaXRzLWxvbmc=";
    private int jwtExpirationMs = 86400000;

    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateJwtTokenFromUsername(UserDetails userDetails) {
        // Generate JWT token with roles as claims
        String username = userDetails.getUsername();
        return Jwts.builder()
                .subject(username)
                // convert GrantedAuthority to string list kyuki spring me as a GrantedAuthority stored hota hai
                //aur jwt me string stored karna hai hame
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority -> GrantedAuthority.getAuthority())
                        .toList())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(keys())
                .compact();
    }

    public boolean validateJwtToken(String jwtToken) {
        try {
            Jwts.parser().verifyWith((SecretKey) keys()).build().parseSignedClaims(jwtToken);
        } catch (Exception e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
            return false;
        }
        return true;
    }

    private Key keys() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String jwtToken) {

        return Jwts.parser().verifyWith((SecretKey) keys()).build()
                .parseSignedClaims(jwtToken)
                .getPayload()
                .getSubject();
    }

    public Claims getAllClaimsFromToken(String jwtToken) {
        return Jwts.parser().verifyWith((SecretKey) keys()).build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }
}


