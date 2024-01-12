package com.oop.projectmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.oop.projectmanagement.FirebaseInitializer;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpSession;

@Controller
public class ProfileController {

    @Autowired
    private FirebaseInitializer firebaseInitializer;


    @GetMapping("/profile")
    public String getUserinfo(HttpSession session,Model model) {
        Firestore db = firebaseInitializer.getDb();
        String documentId = "DvqGIw0kkbAh6tGxoNsu";

        DocumentReference documentRef = db.collection("useraccount").document(documentId);

        try {
            ApiFuture<DocumentSnapshot> documentSnapshotFuture = documentRef.get();
            DocumentSnapshot document = documentSnapshotFuture.get();

            if (document.exists()) {
                // Document found, get required fields
                String username = document.getString("username");
                String firstName = document.getString("firstName");
                String lastName = document.getString("lastName");
                String bio = document.getString("bio");
                String tag = document.getString("tag");
                String facebook = document.getString("facebook"); // Fetching Facebook URL
                String instagram = document.getString("instagram"); // Fetching IG URL


                // Add the fields to the model
                model.addAttribute("username", username);
                model.addAttribute("firstName", firstName);
                model.addAttribute("lastName", lastName);
                model.addAttribute("bio", bio);
                model.addAttribute("tag", tag);
                model.addAttribute("facebook", facebook); // Adding Facebook URL to the model
                model.addAttribute("instagram", instagram); // Adding IG URL to the model
            } else {
                // Document not found
                // Handle the case where the document does not exist
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return "profile";
    }

    @PutMapping("/updateUserProfile")
    public ResponseEntity<String> updateUserProfile(@RequestBody Map<String, Object> data) {
        Firestore db = firebaseInitializer.getDb();
        String documentId = "DvqGIw0kkbAh6tGxoNsu";

        DocumentReference documentRef = db.collection("useraccount").document(documentId);

        try {
            ApiFuture<DocumentSnapshot> documentSnapshotFuture = documentRef.get();
            DocumentSnapshot document = documentSnapshotFuture.get();

            if (document.exists()) {
                // Document found, update required fields
                documentRef.update(data);

                return new ResponseEntity<>("Profile updated successfully", HttpStatus.OK);
            } else {
                // Document not found
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error updating profile", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}