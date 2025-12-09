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

    @OneToMany
    private List<ModuleRole> userRoles;

    private String code;
    private String title;
    private boolean archived;

    @OneToMany
    private List<Assessment> assessments;

    @OneToMany(mappedBy = "targetModule")
    private List<ModuleLog> logs;


    public Module() {}
    public Module(String code, String title, boolean archived) {
        this.code = code;
        this.title = title;
        this.archived = archived;
    }


    public void setID(UUID id) { this.id = id; }
    public void setCode(String newCode) {
        this.code = newCode;
    }
    public void setTitle(String newTitle) {this.title = newTitle;}
    public void setArchived(boolean newArchived) {this.archived = newArchived;}


    public UUID getID() {return id;}
    public List<ModuleRole> getUserRoles() {return userRoles;}
    public String getCode() {return code;}
    public String getTitle() {return title;}
    public boolean getArchiveStatus() {return archived;}
    public List<Assessment> getAssessments() {return assessments;}

    public void addAssessment(Assessment  assessment) {
        this.assessments.add(assessment);
    }
    public void changeArchiveStatus() {this.archived = !this.archived;}
}
