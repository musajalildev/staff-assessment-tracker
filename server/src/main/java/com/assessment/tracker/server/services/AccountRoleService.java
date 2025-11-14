package com.assessment.tracker.server.services;

import com.assessment.tracker.server.persistence.domain.AccountRole;
import com.assessment.tracker.server.persistence.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class AccountRoleService {

    private static final String ROLE_NOT_FOUND = "Role does not exist";
    private final AccountRepository accountRepository;

    public AccountRoleService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountRole createAccountRole(AccountRole accountRole) {
        return accountRepository.save(accountRole);
    }

    public AccountRole getAccountRole(int id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,ROLE_NOT_FOUND));
    }

    public void deleteAccountRole(int id) {
        if(!accountRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,ROLE_NOT_FOUND);
        accountRepository.deleteById(id);
    }

}
