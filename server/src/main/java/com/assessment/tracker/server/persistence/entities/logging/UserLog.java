package com.assessment.tracker.server.persistence.entities.logging;

import jakarta.persistence.*;
import java.util.*;

import com.assessment.tracker.server.persistence.entities.*;
import com.assessment.tracker.server.utils.enums.*;;

@Entity
public class UserLog extends Log {

    @ManyToOne
    @JoinColumn(name = "targetUser")
    private User targetUser;

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }

    public UserActions getActionType() {
        return actionType;
    }

    public void setActionType(UserActions actionType) {
        this.actionType = actionType;
    }

    private UserActions actionType;
}
