package com.crista.securityconfig;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.crista.exceptions.UserNameAlreadyExistsException;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter  extends OncePerRequestFilter{

	@Autowired
	private UserValidationService jwtUserDetailsService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		 String requestTokenHeader = request.getHeader("Authorization");
		System.out.println("entered here ");
		System.out.println(" token header " + requestTokenHeader);
		String username = null;
		String jwtToken = null;
	    String bearer  = null;
	     if(requestTokenHeader != null) {
	      bearer  =  requestTokenHeader.substring(7);	
	       }
	       if(bearer!=null && bearer.equalsIgnoreCase("Bearer ")) {
	    	System.out.println("bearer "+bearer);
	    	requestTokenHeader=requestTokenHeader.replace(bearer,"") ;
	         }
		// JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
		//String bearerCheck  = requestTokenHeader.substring(8,15) ;
		 System.out.println("about to validate  token "+requestTokenHeader+" check "+ bearer);
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {		
			jwtToken = requestTokenHeader.substring(7); //check this .it should  be seven
			System.out.println("jwttoken "+ jwtToken);
			try {
				System.out.println("before username "+ username);
				System.out.println("token jwt "+jwtToken);
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
				System.out.println(" username "+ username);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
			}catch(InternalAuthenticationServiceException in) {
				  throw new UserNameAlreadyExistsException("Possibly user2 email/password does not exists") ;
		       }
			}else {
			logger.warn("JWT Token does not begin with Bearer String");	
			//throw new UnAuthorizedRequestExceptions("Possibly user email/password does not exists") ;
		}
		//Once we get the token, validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("in auth " + username);
			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

			// if token is valid configure Spring Security to manually set authentication
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
	           System.out.println(userDetails.getAuthorities());
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//				SecurityContextHolder.getContext().getAuthentication().
			}
		}
		chain.doFilter(request, response);
	}

	
}







































