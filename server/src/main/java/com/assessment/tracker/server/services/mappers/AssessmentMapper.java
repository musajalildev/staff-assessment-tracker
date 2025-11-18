package com.assessment.tracker.server.services.mappers;

import com.assessment.tracker.server.api.assessment.AssessmentDTO;
import com.assessment.tracker.server.persistence.assessment.Assessment;
import com.assessment.tracker.server.utils.Mapper;
import org.springframework.stereotype.Component;

@Component
public class AssessmentMapper implements Mapper<AssessmentDTO, Assessment> {
    @Override
    public AssessmentDTO entityToApi(Assessment entity) {
        return null;
    }

    @Override
    public Assessment apiToEntity(AssessmentDTO assessmentDTO) {
        return null;
    }
}
