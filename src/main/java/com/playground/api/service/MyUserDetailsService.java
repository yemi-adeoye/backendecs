package com.playground.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.playground.api.model.User;
import com.playground.api.repositories.UserRepository;

@Service 
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findUserByUsername(username);
		if(user == null) {
			throw new UsernameNotFoundException("User details Invalid");
		}
		/* Convert User role into authority */
		List<GrantedAuthority> list = new ArrayList<>();
		
		SimpleGrantedAuthority sga = new SimpleGrantedAuthority(user.getRole());
		list.add(sga);
		
		org.springframework.security.core.userdetails.User springUser = 
				new org.springframework.security.core.userdetails.User
				(user.getUsername(),user.getPassword(),list );
		
		return springUser;
	}

}
/* 
 @Service
 @Component
 */