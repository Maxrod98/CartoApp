package com.ecarto.cartoapp.web.Services;

public class UserRequestDTO {
    String user;
    String passwordHash;

    public UserRequestDTO(String user, String passwordHash){
        this.user = user;
        this.passwordHash = passwordHash;
    }

    public UserRequestDTO(){
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
