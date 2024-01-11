package com.oop.projectmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import javax.servlet.http.HttpSession;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.oop.projectmanagement.FirebaseInitializer;

@Controller
public class StudentHomeController {
    private String username;
    private String firstName;
    private String lastName;
    @Autowired
    private FirebaseInitializer firebaseInitializer;

    
    @GetMapping("/homestudent")

    public String getUserinfo(HttpSession session) {
        username = (String) session.getAttribute("username");
        firstName = (String) session.getAttribute("firstName");
        lastName = (String) session.getAttribute("lastName");
        // Now you can use the username, firstName, and lastName
        return "homestudent"; 
       
    }




}
