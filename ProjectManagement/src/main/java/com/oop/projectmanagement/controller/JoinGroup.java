package com.oop.projectmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpSession;
import com.oop.projectmanagement.FirebaseInitializer;

@Controller
public class JoinGroup {

    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/joingroup")

    public String getUserinfo(HttpSession session) {
        String username = (String) session.getAttribute("username");
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        // Now you can use the username, firstName, and lastName
        return "joingroup";

    }
}