package com.example.omborboshqaruv.Models;

public class User {
    private int id;
    private String email;
    private String full_name;

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    private boolean is_active;


    public String getPhone() {
        return phone;
    }

    private  String phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }



    public String getFull_name() {
        return full_name;
    }



    public String getRole() {
        return role;
    }



    private String role;
}
