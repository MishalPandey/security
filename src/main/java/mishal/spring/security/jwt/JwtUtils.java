//package mishal.spring.security.jwt;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Date;
//
//@Component // this class is managed by spring container as a bean, only spring will create and manage its lifecycle
//
//public class JwtUtils {
//    //https://www.jwt.io/ check this website for more info about JWT
//
//    private String jwtSecret = "YS1zdHJpbmctc2VjcmV0LWF0LWxlYXN0LTI1Ni1iaXRzLWxvbmc="; //B-64 value of this a-string-secret-at-least-256-bits-long
//    // secret key for signing JWTs
//    private int jwtExpirationMs = 86400000; // JWT expiration time in milliseconds (1 day)
//
//    public String getJwtFromHeader() {
//        return "";
//    }
//
//    public String generateJwtTokenFromUsername(String username) {
//
//        return Jwts.builder()
//                .subject(username)
//                .issuedAt(new Date())
//                .expiration(new Date(new Date().getTime() + jwtExpirationMs))
//                .signWith(keys())
//                .compact();
//    }
//
//    public boolean validateJwtToken(String token) {
//        return true;
//    }
//
//    private Key keys() {
//        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
//    }
//}
//
//
//
