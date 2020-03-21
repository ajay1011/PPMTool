package com.ajay.ppmtool.security;

import static com.ajay.ppmtool.security.SecurityConstants.EXPIRATION_TIME;
import static com.ajay.ppmtool.security.SecurityConstants.SECRET;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.ajay.ppmtool.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;


@Component
public class JWTTokenProvider {

	// generate the token
	
	public String generateToken(Authentication authentication) {
	    
	        User user = (User)authentication.getPrincipal();
	        Date now = new Date(System.currentTimeMillis());

	        Date expiryDate = new Date(now.getTime()+EXPIRATION_TIME);

	        String userId = Long.toString(user.getId());
	        
	        System.out.println("Id in gen token "+userId);

	        Map<String,Object> claims = new HashMap<>();
	        claims.put("id", (Long.toString(user.getId())));
	        claims.put("username", user.getUsername());
	        claims.put("fullName", user.getFullName());

	        return Jwts.builder()
	                .setSubject(userId)
	                .setClaims(claims)
	                .setIssuedAt(now)
	                .setExpiration(expiryDate)
	                .signWith(SignatureAlgorithm.HS512, SECRET)
	                .compact();
	        }

	   
	// validate the token
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
			return true;
		}
		catch (SignatureException ex){
            System.out.println("Invalid JWT Signature");
        }catch (MalformedJwtException ex){
            System.out.println("Invalid JWT Token");
        }catch (ExpiredJwtException ex){
            System.out.println("Expired JWT token");
        }catch (UnsupportedJwtException ex){
            System.out.println("Unsupported JWT token");
        }catch (IllegalArgumentException ex){
            System.out.println("JWT claims string is empty");
        }
		return false;
	}
	
	//get user id from token
	
	public Long getUserIdFromJWT(String token) {
		
		Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		
		System.out.println(claims.toString());
		
		String id = (String)claims.get("id");
		
		System.out.println("Id in get id from jwt "+ id);
		
		return Long.parseLong(id);
	}
}
