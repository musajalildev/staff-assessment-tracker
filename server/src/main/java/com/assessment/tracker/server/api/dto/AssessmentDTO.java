package com.assessment.tracker.server.api.dto;

import com.assessment.tracker.server.utils.enums.*;

public class AssessmentDTO {
    private Integer id;
    private ModuleDTO module;
    private AssessmentType type;
    private String title;
    private boolean teamMarked;
    private boolean autoGraded;
    private UserDTO setter;
    private UserDTO checker;
    private AssessmentProgress progress;

    public AssessmentProgress getProgress() {
        return progress;
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
