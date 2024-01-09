package com.oop.projectmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.oop.projectmanagement.FirebaseInitializer;
//import com.oop.projectmanagement.model.User;
@Controller
public class homestaffController {

    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/homestaff")
    public String portfileUser(Model model) {

        return "homestaff";
    }
}