package com.playground.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.playground.api.enums.TicketStatus;
import com.playground.api.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long>{

	@Query("select t from Ticket t where t.employee.user.username=?1 AND t.status=?2")
	List<Ticket> getAllTicketsByUsernameAndStatus(String username, TicketStatus statusEnum);

	@Query("select t from Ticket t where t.status=?1")
    List<Ticket> findAllByStatus(TicketStatus status);

}
