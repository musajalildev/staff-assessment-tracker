package com.assessment.tracker.server.api.DTO;

import java.util.List;

import com.assessment.tracker.server.api.controller.*;
import com.assessment.tracker.server.api.controllerImpl.*;
import com.assessment.tracker.server.api.DTO.*;

import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.persistence.repos.*;
import com.assessment.tracker.server.persistence.services.*;

import com.assessment.tracker.server.utils.mappers.*;
import com.assessment.tracker.server.utils.enums.*;

public class ModuleDTO {
    private String id;
    private String code;
    private String title;
    private List<AssessmentDTO> assessments;
    private boolean archived;

    public ModuleDTO() {
    }

    public ModuleDTO(String id, String code, String title, List<AssessmentDTO> assessments, boolean archived) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AssessmentDTO> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<AssessmentDTO> assessments) {
        this.assessments = assessments;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
