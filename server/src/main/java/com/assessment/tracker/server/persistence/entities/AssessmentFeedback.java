package com.assessment.tracker.server.persistence.entities;

import jakarta.persistence.*;
import com.assessment.tracker.server.utils.enums.*;

import java.time.LocalDateTime;

/**
 * Entity class for feedback on assessment
 * Primary key of type Integer with name ID
 * 
 * It has a many to one relationship with Assessment
 * It has a many to one relationshup with Users through authorID
 * It has an enum of FeedbackType to represent if it was from an exam officer,
 * external examiner, response or checker.
 * Additionally it has a String for feedback, a LocalDateTime for creation time
 */
@Entity
public class AssessmentFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @ManyToOne
    @JoinColumn(name = "assessmentID")
    private Assessment assessment;

    @ManyToOne
    @JoinColumn(name = "authorID")
    private User author;

    private String feedback;

    private FeedbackType type;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

    public FeedbackType getFeedbackType() {
        return type;
    }

    public void setFeedbackType(FeedbackType feedbackType) {
        this.type = feedbackType;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public FeedbackType getType() {
        return type;
    }

    public void setType(FeedbackType type) {
        this.type = type;
    }

}
