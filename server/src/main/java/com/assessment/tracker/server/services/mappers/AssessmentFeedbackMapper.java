package com.assessment.tracker.server.services.mappers;

import com.assessment.tracker.server.api.assessmentFeedback.AssessmentFeedback;
import com.assessment.tracker.server.api.assessmentFeedback.AssessmentFeedbackDTO;
import com.assessment.tracker.server.utils.Mapper;
import org.springframework.stereotype.Component;

@Component
public class AssessmentFeedbackMapper implements Mapper<AssessmentFeedbackDTO, AssessmentFeedback> {
    @Override
    public AssessmentFeedbackDTO entityToApi(AssessmentFeedback entity) {
        return null;
    }

    @Override
    public AssessmentFeedback apiToEntity(AssessmentFeedbackDTO assessmentDTO) {
        return null;
    }
}
