package com.oop.projectmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpSession;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.oop.projectmanagement.FirebaseInitializer;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class GroupDetail {

    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/groupdetail")

    public String getUserinfo(HttpSession session) {
        String username = (String) session.getAttribute("username");
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        // Now you can use the username, firstName, and lastName
        return "groupdetail";

    }
    
    @PostMapping("/deleteGroup")
    @ResponseBody
    public String deleteGroup(@RequestParam String groupName) {
        Firestore firestore = firebaseInitializer.getFirestore();
        DocumentReference groupRef = firestore.collection("group").document(groupName);

        try {
            // Delete the group document
            WriteResult writeResult = groupRef.delete().get();
            return "Group deleted successfully";
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "Error deleting group: " + e.getMessage();
        }
    }
}

