package com.oop.projectmanagement.controller;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.google.cloud.Timestamp;

public class User {
    private String username;
    private String firstname;
    private String lastname;    
    private String password;
    private String user_type;

    // no-arg constructor
    public User() {}

    // getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for firstname
    public String getFirstname() {
        return firstname;
    }

    // Setter for firstname
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    // Getter for lastname
    public String getLastname() {
        return lastname;
    }

    // Setter for lastname
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return user_type;
    }

    public void setUserType(String user_type) {
        this.user_type = user_type;
    }

    public Timestamp getRegDate() {
        return Timestamp.now();
    }

}