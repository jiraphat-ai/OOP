package com.oop.projectmanagement.controller;

import com.google.cloud.firestore.Firestore;
import com.oop.projectmanagement.model.Group;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import com.oop.projectmanagement.FirebaseInitializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class CreateGroup {

    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/creategroup")

    public String getUserinfo(HttpSession session) {
        String username = (String) session.getAttribute("username");
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        // Now you can use the username, firstName, and lastName
        return "creategroup";

    }
    @PostMapping("/createGroup")
    @ResponseBody
    public String createGroup(@ModelAttribute Group group) {
        try {
            Firestore db = firebaseInitializer.getDb();
            db.collection("group").add(group);
            return "Group created successfully";
        } catch (Exception e) {
            return "Error creating group: " + e.getMessage();
        }
    }


}