package com.ecarto.cartoapp.database.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ecarto.cartoapp.web.DTOs.UserDTO;

import io.reactivex.annotations.NonNull;

@Entity
public class UserEntity {
    @PrimaryKey
    @NonNull
    Integer UserId;
    String Username;
    String PasswordHash;
    Integer AccessLevel;

    public UserEntity(){
    }
    public UserEntity(UserDTO userDTO){
        this.setAccessLevel(userDTO.getAccessLevel());
        this.setPasswordHash(userDTO.getPasswordHash());
        this.setUserId(userDTO.getUserId());
        this.setUsername(userDTO.getUsername());
    }

    @NonNull
    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(@NonNull Integer userId) {
        UserId = userId;
    }
    @NonNull
    public String getUsername() {
        return Username;
    }

    public void setUsername(@NonNull String username) {
        Username = username;
    }

    public String getPasswordHash() {
        return PasswordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.PasswordHash = passwordHash;
    }

    public Integer getAccessLevel() {
        return AccessLevel;
    }

    public void setAccessLevel(Integer accessLevel) {
        this.AccessLevel = accessLevel;
    }
}
