package com.assessment.tracker.server.api.dto;

import com.assessment.tracker.server.utils.enums.UserType;
import java.util.UUID;

public class UserDTO {

    private UUID userID;
    private String username;
    private String email;
    private UserType userType;
    private String password;

    public UserDTO() {
    }

    public UserDTO(UUID userID, String username, String email, UserType userType, String password) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.userType = userType;
        this.password = password;
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    // Added for compatibility with update operations
    public String getPassword() {
        return password;
    }
}
