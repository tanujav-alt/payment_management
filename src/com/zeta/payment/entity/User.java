package com.zeta.payment.entity;

import com.zeta.payment.entity.enums.UserRole;

public class User {
    private String userId;
    private String username;
    private String password;
    private UserRole role;

    public User(){

    }

    public User(String userId, String username, String password, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
