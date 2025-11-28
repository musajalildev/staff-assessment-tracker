package com.assessment.tracker.server.utils.mappers;

import com.assessment.tracker.server.api.controller.*;
import com.assessment.tracker.server.api.controllerImpl.*;
import com.assessment.tracker.server.api.DTO.*;

import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.repos.*;
import com.assessment.tracker.server.persistence.services.*;

import com.assessment.tracker.server.utils.mappers.*;
import com.assessment.tracker.server.utils.enums.*;


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
