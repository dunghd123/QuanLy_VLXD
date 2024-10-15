package com.example.quanly_vlxd.jwt;

import com.example.quanly_vlxd.model.UserCustomDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenProvider {
    @Value("${com.jwt.secret}")
    private String JWT_SECRET;
    @Value("${com.jwt.expiration}")
    private int JWT_EXPIRATION;
    @Value("${com.jwt.refresh_expiration}")
    private String JWT_REFRESH_EXPIRATION;

    public String extractUsername(String jwtToken){
        return extractClaim(jwtToken, Claims::getSubject);
    }
    public<T> T extractClaim(String token, Function<Claims,T> claimsTFunction){
        Claims claims=extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }
    public Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public String generateToken(UserCustomDetail userCustomDetail){
        return generateToken(new HashMap<>(),userCustomDetail);
    }
    public String generateToken(Map<String, Object> extracClaims, UserCustomDetail userCustomDetail){
        extracClaims.put("roles", userCustomDetail.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());
        return buildToken(extracClaims, userCustomDetail, JWT_EXPIRATION);
    }
    public String generateRefreshToken(UserCustomDetail userCustomDetail){
        return buildToken(new HashMap<>(),userCustomDetail, Integer.parseInt(JWT_REFRESH_EXPIRATION));
    }
    //xay dung token
    public String buildToken(Map<String, Object> extracClaims,UserCustomDetail userCustomDetail, int expiration){
        return Jwts.builder()
                .claims(extracClaims)
                .subject(userCustomDetail.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ expiration))
                .signWith(getSignInKey())
                .compact();
    }

    // ma hoa chuoi JWT_SECRET
    public SecretKey getSignInKey(){
        byte[] keyBytes= Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    //kiem tra token co valid k
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username= extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    //kiem tra token het han chua
    public boolean isTokenExpired(String token){
        //neu date moi > expiredate => true
        return extractExpiration(token).before(new Date());
    }
    // lay thong tin expiration trong token
    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

}
