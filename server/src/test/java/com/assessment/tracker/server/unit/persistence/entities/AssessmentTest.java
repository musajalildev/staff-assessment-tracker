package com.assessment.tracker.server.unit.persistence.entities;

import com.assessment.tracker.server.configuration.RsaKeyProperties;
import com.assessment.tracker.server.persistence.entities.Assessment;
import com.assessment.tracker.server.utils.enums.AssessmentProgress;
import com.assessment.tracker.server.utils.enums.AssessmentType;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@DataJpaTest
public class AssessmentTest extends EntityTest<Assessment> {

    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @MockitoBean
    private RsaKeyProperties rsaKeyProperties;

    @Override
    protected Assessment createEntity() {
        return new Assessment(AssessmentType.COURSEWORK, AssessmentProgress.CREATED, "Epic Assessment");
    }

    @Override
    protected Object getId(Assessment entity) {
        return entity.getId();
    }
}
