package com.assessment.tracker.server.persistence.services.logging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assessment.tracker.server.persistence.entities.logging.*;
import com.assessment.tracker.server.persistence.repos.logging.*;

@Service
public class UserLogService {
    private final UserLogRepository logRepository;

    @Autowired
    public UserLogService(UserLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public void save(UserLog log) {
        logRepository.save(log);
    }

}
