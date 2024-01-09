package com.oop.projectmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.oop.projectmanagement.FirebaseInitializer;

@Controller
public class LoginController {

    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/login")
    public String login(Model model) {

        Firestore db = firebaseInitializer.getDb();
        DocumentReference docRef = db.collection("testCollection").document("testDocument");
        DocumentSnapshot document;
        try {
            document = docRef.get().get();
            if (document.exists()) {
                model.addAttribute("firebaseTest", "Firebase connection successful. Document data: " + document.getData());
            } else {
                model.addAttribute("firebaseTest", "No such document in Firestore.");
            }
        } catch (Exception e) {
            model.addAttribute("firebaseTest", "Failed to connect to Firestore: " + e.getMessage());
        }
        model.addAttribute("pageTitle", "Welcome to My Application");
        return "login";
    }
}
