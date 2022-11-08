package com.playground.api.repositories;

import com.playground.api.model.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DomainRepository extends JpaRepository<Domain, Long> {

    @Query("select d from Domain d where d.domain=?1")
    Domain findByDomainName(String domain);

    @Query("select d from Domain d where d.domain=?1 AND d.enabled=?2")
    Domain findByDomainNameAndStatus(String domainName, boolean status);
}
