package com.playground.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.playground.api.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

	@Query("select u from User u where u.username =?1")
	User findUserByUsername(String username);
	//is-a


	
	 
}

/* 
   Animal : m1() : interface/abstract 
   Dog extends Animal : m1() : Dog is-a Animal : m2()   IoC
 */