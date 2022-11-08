package com.playground.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.playground.api.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findByName(String adminName);

    List<Admin> findAll();

    @Query("select a from Admin a where a.user.username = ?1")
    Admin getByEmail(String username);

    @Query("select m from Admin m where m.user.enabled=?1")
	List<Admin> findAdminByEnabled(boolean enabled);

    Admin findByUserId(Long id);
    
}
