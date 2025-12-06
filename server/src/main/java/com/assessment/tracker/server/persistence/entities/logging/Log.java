package com.assessment.tracker.server.persistence.entities.logging;

import jakarta.persistence.*;
import java.util.*;

import com.assessment.tracker.server.persistence.entities.User;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ID;

    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;

    private String comment;

    public Log() {
        this.logTime = LocalDateTime.now();
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer iD) {
        ID = iD;
    }

    public User getUserID() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getLogTime() {
        return logTime;
    }

    public void setLogTime(LocalDateTime logTime) {
        this.logTime = logTime;
    }

    private LocalDateTime logTime;

}
