package com.assessment.tracker.server.integration.persistence.entities;

import com.assessment.tracker.server.persistence.entities.Assessment;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AssessmentTest extends EntityTest<Assessment> {
    @Override
    protected Assessment createEntity() {
        return new Assessment();
    }

    @Override
    protected Object getId(Assessment entity) {
        return entity.getID();
    }
}
