package com.assessment.tracker.server.api.DTO;
import java.util.UUID;

import java.util.List;

public class ModuleDTO {
    private UUID id;
    private UUID leaderID;
    private List<UUID> otherStaffIDs;
    private int code;
    private String title;
    private List<AssessmentDTO> assessments;
    private boolean archived;

    public ModuleDTO() {}
    public ModuleDTO(UUID id, int code, String title, List<AssessmentDTO> assessments, boolean archived) {}

    public void setID(UUID id) {this.id = id;}
    public void setLeaderID(UUID newLeaderID) { this.leaderID = newLeaderID; }
    public void setOtherStaffIDs(List<UUID> newOtherStaffIDs) { this.otherStaffIDs = newOtherStaffIDs; }
    public void setCode(int code) {
        this.code = code;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setArchived(boolean archived) {
        this.archived = archived;
    }
    public void setAssessments(List<AssessmentDTO> assessments) {
        this.assessments = assessments;
    }


    public UUID getID() {
        return id;
    }
    public UUID getLeaderID() { return leaderID; }
    public List<UUID> getOtherStaffIDs() {return otherStaffIDs;}
    public int getCode() {
        return code;
    }
    public String getTitle() {
        return title;
    }
    public boolean isArchived() {
        return archived;
    }
    public List<AssessmentDTO> getAssessments() {
        return assessments;
    }


}