package com.crista.securityconfig;

import java.util.Collections;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity()
public class SecurityConfigurer extends WebSecurityConfigurerAdapter{
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserValidationService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// We don't need CSRF for this example
//		list of roles JETADMIN,CUSTOMERADMIN ,JETSUPERVISOR,CUSTOMERSUPERVISOR	
		http.cors()
		.configurationSource(new CorsConfigurationSource() {			
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
			   CorsConfiguration cors  = new  CorsConfiguration() ;
			     cors.setAllowedOriginPatterns(Collections.singletonList("*")) ;
			     cors.setAllowedMethods(Collections.singletonList("*"));
			     cors.setAllowCredentials(true);
			     cors.setAllowedHeaders(Collections.singletonList("*"));
			     cors.setMaxAge(3600L);		     
			     return  cors ;
			       }
		       })
		        .and().csrf().disable()
				.authorizeRequests()
				.antMatchers("/register").permitAll()
				//.antMatchers("/update").permitAll()
				.antMatchers("/authenticate").permitAll()
				.antMatchers("/uploads/**").permitAll()
				.antMatchers("/jetstore/**").permitAll()
				.antMatchers("/customer/register").permitAll()
				.antMatchers("/cartproduct/*").permitAll()
				.antMatchers("/cartproduct/**").permitAll()
				.antMatchers("/subscribe").permitAll()
				.antMatchers("/product/display/*").permitAll()
				.antMatchers("/product/test").permitAll()
				.antMatchers("/product/cartspecial/**").permitAll()
				.antMatchers("/product/cartnewarrival/**").permitAll()
				.antMatchers("/product/cartfeatured/**").permitAll()
				.antMatchers("/product/cartdisplay/**").permitAll()
				.antMatchers("/product/byspecial/**").permitAll()
				.antMatchers("/product/byfeatured").permitAll()
				.antMatchers("/product/bynewarrival").permitAll()
				.antMatchers("/product/loadproduct").permitAll()
				.antMatchers("/product/loadproductcategory/*").permitAll()
				.antMatchers("/product/loadsimilaritems").permitAll()
				.antMatchers("/product/loadsimilaritems2").permitAll()
				.antMatchers("/product/displayandloadsimilaritems/*").permitAll()
				.antMatchers("/product/loadsearchitem").permitAll()
				.antMatchers("/product/loadsearchproduct").permitAll()
				.antMatchers("/product/loadcollections").permitAll()
				.antMatchers("/product/categoryitems").permitAll()
				.antMatchers("/eventbyuser").permitAll()
				.antMatchers("/eventbyvendorname").permitAll()
				.antMatchers("/finduser").authenticated() 
				.antMatchers("/profile/**").authenticated()			
				.antMatchers("/product/product").authenticated()
				.antMatchers("/product/allproduct").authenticated()
				.antMatchers("/product/create").authenticated()
				.antMatchers("/product/find").authenticated()
				.antMatchers("/product/uploadfiles").authenticated()
				.antMatchers("/product/findallproduct").authenticated()
				.antMatchers("/categories/category2").authenticated()
				.antMatchers("/order/create").hasRole("CUSTOMER")
				.antMatchers("/order/order").hasAnyRole("SUPERVISOR","ADMIN")
				.antMatchers("/review/**").hasAnyRole("CUSTOMER","ADMIN")
				.antMatchers("/dispatchEvent").hasRole("ADMIN")
				.antMatchers("/vendor/**").hasRole("ADMIN")
//				.antMatchers("/product/create/*").hasRole("ADMIN")
//				.antMatchers("/product/delete/*").hasRole("ADMIN")
				.antMatchers("/categories/create").hasRole("ADMIN")
				.antMatchers("/categories/category").hasRole("ADMIN")
				.antMatchers("/categories/filereport").hasRole("ADMIN")
				.antMatchers("/coupon/coupon").hasRole("ADMIN")
				.antMatchers("/coupon/findcoupon/**").hasRole("ADMIN")
				.antMatchers("/coupon/deletecoupon").hasRole("ADMIN")
				.antMatchers("/taxes/tax").hasRole("ADMIN")
				.antMatchers("/taxes/updatetax").hasRole("ADMIN")
				.antMatchers("/subcat/**").hasRole("ADMIN")
				.antMatchers("/usersearch").hasRole("ADMIN")
				.antMatchers("/user").hasRole("ADMIN")
				.antMatchers("/vendor/vendor").hasRole("ADMIN")
				.antMatchers("/delete/**").hasRole("ADMIN")
				.antMatchers("/update/**").hasRole("ADMIN")   // there are three roles : admin,supervisor,and customer
				.anyRequest().authenticated()
				.and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.formLogin().loginPage("/adminlogin").permitAll()
				.usernameParameter("username").passwordParameter("password").and().logout() ; // this is used as username in the  html form page
//				.and()
//				.exceptionHandling().accessDeniedPage("/403") ;				
		// Add a filter to validate the tokens with every request
				http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	      }
}
