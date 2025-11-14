package com.assessment.tracker.server.persistence.repository;
import com.assessment.tracker.server.persistence.domain.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountRole, Integer> {
}
