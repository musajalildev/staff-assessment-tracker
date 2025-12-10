package com.assessment.tracker.server.unit.persistence.entities;

import com.assessment.tracker.server.configuration.RsaKeyProperties;
import com.assessment.tracker.server.persistence.entities.Module;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@DataJpaTest
class ModuleTest extends EntityTest<Module>{

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
    protected Module createEntity() {
        return new Module("COM-1001", "Java", false);
    }

    @Override
    protected Object getId(Module entity) {
        return entity.getID();
    }
}
