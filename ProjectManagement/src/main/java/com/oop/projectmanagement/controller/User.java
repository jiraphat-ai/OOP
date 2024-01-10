package com.oop.projectmanagement.controller;

import com.google.cloud.Timestamp;

public class User {
    private String student_id;
    private String first_name;
    private String last_name;
    private String password;
    private String user_type;

    // no-arg constructor
    public User() {}

    // getters and setters
    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
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
