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

        DocumentReference documentRef = db.collection("useraccount").document();

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
    public ResponseEntity<String> updateUserProfile(@RequestBody Map<String, Object> data, HttpSession session) {
        Firestore db = firebaseInitializer.getDb();

        // Retrieve the username from the session
        String username = (String) session.getAttribute("username");

        // Ensure a valid username is available in the session
        if (username == null) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }

        // Fetch the document based on the username
        ApiFuture<QuerySnapshot> querySnapshotFuture = db.collection("useraccount").whereEqualTo("username", username).limit(1).get();

        try {
            QuerySnapshot querySnapshot = querySnapshotFuture.get();

            if (!querySnapshot.isEmpty()) {
                // Document found, update required fields
                DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                DocumentReference documentRef = document.getReference();
                documentRef.update(data);

                // Update session attributes with new information
                session.setAttribute("bio", (String) data.get("bio"));
                session.setAttribute("facebook", (String) data.get("facebook"));
                session.setAttribute("instagram", (String) data.get("instagram"));

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