package com.playground.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.playground.api.model.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Long>{

	@Query("select m from Manager m where m.user.username=?1")
	Manager getByEmail(String managerEmail);

	@Query("select m from Manager m where m.name=?1")
	Manager findManagerByName(String managerName);

	@Query("select m from Manager m where m.user.enabled=?1")
	List<Manager> findManagerByEnabled(boolean enabled);

	Manager findByUserId(Long id);

}
