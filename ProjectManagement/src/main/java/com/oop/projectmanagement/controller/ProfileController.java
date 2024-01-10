package com.oop.projectmanagement.controller;

import com.oop.projectmanagement.FirebaseInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/profile")
    public String profile(Model model) {

        return "profile";
    }
}
