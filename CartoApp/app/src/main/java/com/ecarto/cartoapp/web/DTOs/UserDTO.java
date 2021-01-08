package com.ecarto.cartoapp.web.DTOs;

import java.util.List;

public class UserDTO {
    Integer UserId;
    String Username;
    String PasswordHash;
    Integer AccessLevel;
    List<ProjectDTO> projectDTOs;

    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPasswordHash() {
        return PasswordHash;
    }

    public void setPasswordHash(String passwordHash) {
        PasswordHash = passwordHash;
    }

    public Integer getAccessLevel() {
        return AccessLevel;
    }

    public void setAccessLevel(Integer accessLevel) {
        AccessLevel = accessLevel;
    }
}
