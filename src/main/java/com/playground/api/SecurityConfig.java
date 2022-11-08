package com.playground.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.playground.api.service.MyUserDetailsService;
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired	
	private MyUserDetailsService myUserDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		/*  
		auth.inMemoryAuthentication()
		 .passwordEncoder(getEncoder())
		 .withUser("harry@gmail.com").password(getEncoder().encode("potter@123")).authorities("EMPLOYEE")
		 .and()
		 .withUser("ronald@gmail.com").password(getEncoder().encode("weasley@123")).authorities("EMPLOYEE")
		 .and()
		 .withUser("albus@gmail.com").password(getEncoder().encode("albus@123")).authorities("MANAGER")
		 .and()
		 .withUser("severus@gmail.com").password(getEncoder().encode("snape@123")).authorities("MANAGER");
		*/
		auth.authenticationProvider(getCustomAuth());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	 
		 http

		 .authorizeRequests()

		 	.antMatchers(HttpMethod.GET, "/api/manager/all").permitAll()
		 	.antMatchers(HttpMethod.POST, "/api/employee/add").permitAll()
		 	.antMatchers(HttpMethod.GET, "/api/employee/all").hasAuthority("MANAGER")
		 	.antMatchers(HttpMethod.GET, "/api/auth/login").permitAll()
		 	.antMatchers(HttpMethod.GET, "/api/auth/user").authenticated()
		 	.antMatchers(HttpMethod.POST, "/api/leave/add").hasAnyAuthority("EMPLOYEE","MANAGER")
		 	.antMatchers(HttpMethod.POST, "/api/ticket/add").hasAnyAuthority("EMPLOYEE","MANAGER")
		 	.antMatchers(HttpMethod.GET, "/api/ticket/priority/all").permitAll()
		 	.antMatchers(HttpMethod.GET, "/api/employee/access").hasAnyAuthority("MANAGER", "ADMIN")
		 	.antMatchers(HttpMethod.GET, "/api/user/grant-access/{email}").hasAuthority("MANAGER")
		 	.antMatchers(HttpMethod.GET, "/api/leave/all").hasAuthority("MANAGER")
		 	.antMatchers(HttpMethod.GET, "/api/update-status/{leaveID}/{leaveStatus}").hasAuthority("MANAGER")
		 	.anyRequest().permitAll()
		 	.and().httpBasic()
		 	.and().csrf().disable();
	}
	
	
	@Bean
	public PasswordEncoder getEncoder(){
		 
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder; 
	}
	
	private DaoAuthenticationProvider getCustomAuth(){
		DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
		dao.setPasswordEncoder(getEncoder());
		dao.setUserDetailsService(myUserDetailsService);
		return dao;
	}
}
