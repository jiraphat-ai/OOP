package com.oop.projectmanagement.controller;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class User {
    private String username;
    private String firstname;
    private String lastname;

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
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
//pls don't delete our fetch naja
    @PostMapping("/login")
    public String login(HttpSession session, Model model) {
        // Validate the username and password here
        String username = (String) session.getAttribute("username");
        // If validation is successful, add the username to the model 
        model.addAttribute("username", username);
        return "redirect:/homestudent";
    }
}
