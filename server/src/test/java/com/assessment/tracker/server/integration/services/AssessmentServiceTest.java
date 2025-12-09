package com.assessment.tracker.server.integration.services;

import com.assessment.tracker.server.persistence.services.AssessmentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AssessmentServiceTest {
    @Autowired
    private AssessmentService assessmentService;

    @BeforeEach
    public void setUp() {
    }
    @AfterEach
    public void tearDown() {
    }
}
