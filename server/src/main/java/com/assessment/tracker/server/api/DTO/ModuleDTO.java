package com.assessment.tracker.server.api.DTO;

import java.util.UUID;

import java.util.List;

public class ModuleDTO {
    private UUID id;
    private String code;
    private String title;
    private boolean archived;


    public ModuleDTO() {}
    public ModuleDTO(UUID id, int code, String title, List<AssessmentDTO> assessments, boolean archived) {}

    public void setID(UUID id) {this.id = id;}
    public void setCode(String code) {
        this.code = code;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setArchived(boolean archived) {
        this.archived = archived;
    }


    public UUID getID() {
        return id;
    }
    public String getCode() {
        return code;
    }
    public String getTitle() {
        return title;
    }
    public boolean isArchived() {
        return archived;
    }
}