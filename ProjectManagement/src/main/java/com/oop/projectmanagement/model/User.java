package com.oop.projectmanagement.model;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import com.google.cloud.Timestamp;

public class User {
    private String username;
    private String first_name;
    private String last_name;
    private String password;
    private String user_type;
    private String bio;
    private String instagram;
    private String facebook;
     private ArrayList<String> tag;

//    private String documentId;

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
    public String getFirstName() {
        return first_name;
    }

    // Setter for firstname
    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    // Getter for lastname
    public String getLastName() {
        return last_name;
    }

    // Setter for lastname
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

//    public void setDocumentId(String documentId) {
//        this.documentId = documentId;
//    }
//
//    public String getDocumentId() {
//        return documentId;
//    }
    
        public void setBio(String bio) {
            this.bio = bio;
        }
    
        public String getBio() {
            return bio;
        }
    
        public void setInstagram(String instagram) {
            this.instagram = instagram;
        }
    
        public String getInstagram() {
            return instagram;
        }
    
        public void setFacebook(String facebook) {
            this.facebook = facebook;
        }
    
        public String getFacebook() {
            return facebook;
        }
        public void setTag(ArrayList<String> tag) {
            this.tag = tag;
        }
    
        public ArrayList<String> getTag() {
            return tag;
        }
}