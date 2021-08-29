package com.crista.securityconfig;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.crista.enties.Customer;
import com.crista.enties.Users;
import com.crista.repositories.CustomerRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {
	public static final long JWT_TOKEN_VALIDITY = 5*60*60;
	@Autowired
	UserValidationRepository userRepo ;
	
	@Autowired
	CustomerRepository customerRepo ;
	
	@Value("${jwt.secret}")
	private String secret;
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
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean ignoreTokenExpiration(String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		String username = userDetails.getUsername() ; // this  is an actually the email in the repository .
		Users theUser = userRepo.getByEmail(username) ;
		claims.put("username",theUser.getFirstname()) ;
		claims.put("ack",theUser.getId()) ;
		claims.put("vendorname",theUser.getVendorname()) ;
//		claims.put("useremail",theUser.getEmail()) ;
		System.out.println("in userdetails " + userDetails.getUsername()+" claims "+claims.toString()) ;
		return doGenerateToken(claims, userDetails.getUsername());
	}
	
	public String generateCustomerToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		String username = userDetails.getUsername() ; // this  is an actually the email in the repository .
		Customer theUser = customerRepo.getByEmail(username) ;
		claims.put("username",theUser.getFirstname()) ;
		claims.put("ack",theUser.getId()) ;
		//claims.put("vendorname",theUser.getVendorname()) ;
//		claims.put("useremail",theUser.getEmail()) ;
		System.out.println("in userdetails " + userDetails.getUsername()+" claims "+claims.toString()) ;
		return doGenerateToken(claims, userDetails.getUsername());
	}
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).
				setIssuedAt(new Date(System.currentTimeMillis()))
		.setExpiration(new Date(System.currentTimeMillis() + 
				JWT_TOKEN_VALIDITY*1000)).
		       signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
