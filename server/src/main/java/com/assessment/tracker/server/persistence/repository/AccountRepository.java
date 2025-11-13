package com.assessment.tracker.server.persistence.repository;
import com.assessment.tracker.server.persistence.domain.Account_Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account_Role, Integer> {
}
