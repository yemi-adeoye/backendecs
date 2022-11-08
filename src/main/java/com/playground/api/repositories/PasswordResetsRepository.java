package com.playground.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.playground.api.model.PasswordResets;
import com.playground.api.model.User;

public interface PasswordResetsRepository extends JpaRepository<PasswordResets, Long>{

	User findUserByRand(String rand);

	PasswordResets findUserIdByRand(String rand);

	@Query("select p from PasswordResets p where p.rand=?1 and p.user.username=?2")
	PasswordResets getByOtaAndEmail(String ota, String email);

}