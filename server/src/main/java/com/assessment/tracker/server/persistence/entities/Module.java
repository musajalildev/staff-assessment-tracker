package com.assessment.tracker.server.persistence.entities;

import com.assessment.tracker.server.persistence.entities.logging.ModuleLog;

import jakarta.persistence.*;
import java.util.UUID;
import java.util.List;

@Entity
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID leaderID;
    private List<UUID> otherStaffIDs;
    private int code;
    private String title;
    private boolean archived;

    @OneToMany
    private List<Assessment> assessments;

    @OneToMany(mappedBy = "targetModule")
    private List<ModuleLog> logs;

    public void setID(UUID id) { this.id = id; }
    public void setLeaderID(UUID newLeaderID) {
        this.leaderID = newLeaderID;
    }
    public void setOtherStaffIDs(List<UUID> newOtherStaffIDs) {this.otherStaffIDs = newOtherStaffIDs;}
    public void setCode(int newCode) {
        this.code = newCode;
    }
    public void setTitle(String newTitle) {this.title = newTitle;}
    public void setArchived(boolean newArchived) {this.archived = newArchived;}
    public void setAssessments(List<Assessment> newAssessments) {this.assessments = newAssessments;}


    public UUID getID() {return id;}
    public UUID getLeaderID() {return leaderID;}
    public List<UUID> getOtherStaffIDs() {return otherStaffIDs;}
    public int getCode() {return code;}
    public String getTitle() {return title;}
    public boolean getArchiveStatus() {return archived;}
    public List<Assessment> getAssessments() {return assessments;}

    public void addAssessment(Assessment  assessment) {
        this.assessments.add(assessment);
    }
    public void changeArchiveStatus() {this.archived = !this.archived;}
}
