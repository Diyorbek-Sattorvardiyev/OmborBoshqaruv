package com.example.omborboshqaruv.Models;

public class LoginResponse {
    private String access_token;
    private User user;

    public String getAccess_token() {
        return access_token;
    }

    public User getUser() {
        return user;
    }
}