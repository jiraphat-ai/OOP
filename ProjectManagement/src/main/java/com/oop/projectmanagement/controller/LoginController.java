package com.oop.projectmanagement.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.oop.projectmanagement.FirebaseInitializer;
import projectManagementEntity.Account;

@Controller
public class LoginController {


    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageTitle", "Welcome to My Application");
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, @RequestParam("password") String password,
            Model model , HttpSession session) {
        Firestore db = firebaseInitializer.getDb();

        try {
            ApiFuture<QuerySnapshot> query = db.collection("useraccount").get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (DocumentSnapshot document : documents) {
                String storedUsername = document.getString("username");
                String storedPassword = document.getString("password");
                String userType = document.getString("userType"); // Get user type
                if (username.equals(storedUsername) && password.equals(storedPassword)) {
                    if ("student".equals(userType)) {
                        model.addAttribute("message", "Login successful. User type: " + userType);
                        session.setAttribute("username", username);
                        return "redirect:/homestudent"; // Redirect to index page if user is a student
                    } else if ("staff".equals(userType)) {
                        model.addAttribute("message", "Login successful. User type: " + userType);
                        return "redirect:/importuserfile"; // Redirect to homestaff page if user is a staff
                    }
                }
            }
            model.addAttribute("error", "Invalid username or password. Please try again.");
            return "login"; // Redirect back to login page if username or password is incorrect
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "login"; // Redirect back to login page if an exception occurs
        }
    }
}
