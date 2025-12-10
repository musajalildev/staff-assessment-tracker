package com.assessment.tracker.server.unit.entities;

import com.assessment.tracker.server.persistence.entities.Assessment;
import com.assessment.tracker.server.utils.enums.AssessmentProgress;
import com.assessment.tracker.server.utils.enums.AssessmentType;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AssessmentTest extends EntityTest<Assessment> {

    @Override
    protected Assessment createEntity() {
        return new Assessment(AssessmentType.COURSEWORK, AssessmentProgress.CREATED,"Epic Assessment");
    }

    @Override
    protected Object getId(Assessment entity) {
        return entity.getID();
    }
}
