package com.mynoteservice.config;

import com.mynotelibrary.dto.UserRequestDTO;
import com.mynoteservice.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtils {

    private static final long  EXPIRIATION_AT = 1000*60*60;
    private static final Logger log = LoggerFactory.getLogger(JWTUtils.class);
    private final String key = "this is the spring practices code base at token generated key";
    private final SecretKey secretKey= Keys.hmacShaKeyFor(key.getBytes());

    public Map<String, String> generateJwtToken(UserRequestDTO authRequest) {
        try{
            Map<String, String> token = new HashMap<>();
            String generatedToken=getToken(authRequest.getEmail());
            log.info("Generated token: "+generatedToken);
            token.put("token", generatedToken);

            log.info("Generated token map value: "+token.get("token"));
            return token;
        }catch (Exception e) {
            log.error("An exception thrown when generating the jwt token", e);
            throw e;
        }
    }

    private String getToken(String userName) {
        try{
            return  Jwts.builder().setSubject(userName).
                    setIssuedAt(new Date()).
                    setExpiration(new Date(System.currentTimeMillis() + EXPIRIATION_AT)).
                    signWith(secretKey, SignatureAlgorithm.HS256)
                    .compact();

        }catch (Exception e) {
            throw e;
        }
    }

    public String getUserNameFromToken(String token) {
     return  extractToken(token).getSubject();
    }

    private Claims extractToken(String token) {
        return Jwts.parser().
                setSigningKey(secretKey).
                build().
                parseClaimsJws(token).
                getBody();
    }

    public boolean validateToken(String userName, UserEntity userDetails, String token) {
        log.info("userName: "+userName+", user details user name is : "+userDetails.getEmail());
        log.info(" Is user name is valid "+userName.equals(userDetails.getEmail()));
        return userName.equals(userDetails.getEmail()) && isExpired(token);
    }

    // Is it expire or not
    private boolean isExpired(String token) {
        log.info(" expired time  {}, now date time is : {}",extractToken(token).getExpiration(), new Date());
        log.info(" Is token is expired {}",extractToken(token).getExpiration().before(new Date()));
        return extractToken(token).getExpiration().after(new Date());
    }
}
