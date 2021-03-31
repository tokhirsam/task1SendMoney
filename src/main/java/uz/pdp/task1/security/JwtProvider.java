package uz.pdp.task1.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JwtProvider {
    static long expireTime = 36000 * 1000;
    static String secret = "ThisIsTopSecretDontEvenThinkAbout";

    public String generateToken(String userName) {
        return Jwts
                .builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts
                    .parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            return true;
//                 .getBody()
//                 .getSubject()
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public String getUserNameFromToken(String token){
        String username = Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return username;
    }
}
