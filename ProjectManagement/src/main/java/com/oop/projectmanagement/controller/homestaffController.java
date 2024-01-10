package com.oop.projectmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.oop.projectmanagement.FirebaseInitializer;

@Controller
public class homestaffController {

    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/homestaff")
    public String portfileUser(Model model) {

        return "homestaff";
    }

    @PostMapping("/addUser")
    public String addUser(
    @RequestParam("studentId") String studentId,
    @RequestParam("password") String password,
    @RequestParam("firstName") String firstName,
    @RequestParam("lastName") String lastName) {

    Firestore db = firebaseInitializer.getDb(); // Use getDb() instead of getFirebase()

    Map<String, Object> user = new HashMap<>();
    user.put("studentId", studentId);
    user.put("password", password);
    user.put("firstName", firstName);
    user.put("lastName", lastName);

    try {
        ApiFuture<DocumentReference> addedDocRef = db.collection("users").add(user);
    } catch (Exception e) {
        e.printStackTrace();
        return "error";
    }

    return "redirect:/homestaff";

    }
}