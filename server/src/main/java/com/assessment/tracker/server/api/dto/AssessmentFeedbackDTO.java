package com.assessment.tracker.server.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class AssessmentFeedbackDTO {
    private Integer id;
    private Integer assessmentID;
    private String feedback;
    private UserDTO author;
    private LocalDateTime createdDate;
    
    // Convenience fields for frontend compatibility
    private UUID authorID;
    private String authorUsername;

    public AssessmentFeedbackDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(Integer assessmentID) {
        this.assessmentID = assessmentID;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
        // Auto-populate convenience fields when author is set
        if (author != null) {
            this.authorID = author.getUserID();
            this.authorUsername = author.getUsername();
        }
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public UUID getAuthorID() {
        return authorID;
    }

    public void setAuthorID(UUID authorID) {
        this.authorID = authorID;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }
}
