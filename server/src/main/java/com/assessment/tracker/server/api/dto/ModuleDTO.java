package com.assessment.tracker.server.api.dto;

import java.util.List;

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
