package com.assessment.tracker.server.api.dto;

import com.assessment.tracker.server.utils.enums.*;

// DTO to represent Assessment
public class AssessmentDTO {
    // Fields
    private Integer id;
    private ModuleDTO module;
    private AssessmentType type;
    private String title;
    private boolean teamMarked;
    private boolean autoGraded;
    private UserDTO setter;
    private UserDTO checker;
    private AssessmentProgress progress;

    // Getters and Setters

    public AssessmentProgress getProgress() {
        return progress;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isTeamMarked() {
        return teamMarked;
    }

    public void setTeamMarked(boolean teamMarked) {
        this.teamMarked = teamMarked;
    }

    public boolean isAutoGraded() {
        return autoGraded;
    }

    public void setAutoGraded(boolean autoGraded) {
        this.autoGraded = autoGraded;
    }

    public void setProgress(AssessmentProgress progress) {
        this.progress = progress;
    }

    public Integer getID() {
        return id;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public ModuleDTO getModule() {
        return module;
    }

    public void setModule(ModuleDTO module) {
        this.module = module;
    }

    public AssessmentType getType() {
        return type;
    }

    public void setType(AssessmentType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserDTO getSetter() {
        return setter;
    }

    public void setSetter(UserDTO setter) {
        this.setter = setter;
    }

    public UserDTO getChecker() {
        return checker;
    }

    public void setChecker(UserDTO checker) {
        this.checker = checker;
    }
}
