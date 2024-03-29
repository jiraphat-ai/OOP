package com.oop.projectmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.oop.projectmanagement.FirebaseInitializer;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpSession;

@Controller
public class ProfileController extends CustomControl{

    @Autowired
    private FirebaseInitializer firebaseInitializer;


    @GetMapping("/profile")
    public String getUserProfile(HttpSession session, Model model,@RequestParam(required = false) String username) throws InterruptedException, ExecutionException{
        model.addAttribute("tags", getTags());
        Firestore db = firebaseInitializer.getDb();
        if(username == null){
            username = (String) session.getAttribute("username");
        }
        Query documentRef = db.collection("useraccount").whereEqualTo("username", username);

        try {
            ApiFuture<QuerySnapshot> documentSnapshotFuture = documentRef.get();
            QuerySnapshot document = documentSnapshotFuture.get();
            DocumentSnapshot documentSnapshot = document.getDocuments().get(0);

            // Get all the data in the document
            Map<String, Object> userData = documentSnapshot.getData();

            // Add the data to the model
            model.addAttribute("userData", userData);
            List<String> userTags = (List<String>) userData.get("tag");
            model.addAttribute("userTags", userTags);

            // Add the missing fields if they don't exist
            boolean updateNeeded = false;
            if (!userData.containsKey("bio")) {
                userData.put("bio", null);
                updateNeeded = true;
            }
            if (!userData.containsKey("instagram")) {
                userData.put("instagram", null);
                updateNeeded = true;
            }
            if (!userData.containsKey("facebook")) {
                userData.put("facebook", null);
                updateNeeded = true;
            }
            if (!userData.containsKey("tag")) {
                userData.put("tag", new ArrayList<String>());
                updateNeeded = true;
            }
            // If any fields were missing, update the document in Firebase
            if (updateNeeded) {
                documentSnapshot.getReference().set(userData);
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return "profile";
    }
    @GetMapping("/viewprofile")
    public String viewUserProfile(HttpSession session, Model model,@RequestParam(required = false) String username) throws InterruptedException, ExecutionException{
        model.addAttribute("tags", getTags());
        Firestore db = firebaseInitializer.getDb();
        if(username == null){
            username = (String) session.getAttribute("username");
        }
        Query documentRef = db.collection("useraccount").whereEqualTo("username", username);

        try {
            ApiFuture<QuerySnapshot> documentSnapshotFuture = documentRef.get();
            QuerySnapshot document = documentSnapshotFuture.get();
            DocumentSnapshot documentSnapshot = document.getDocuments().get(0);

            // Get all the data in the document
            Map<String, Object> userData = documentSnapshot.getData();

            // Add the data to the model
            model.addAttribute("userData", userData);
            List<String> userTags = (List<String>) userData.get("tag");
            model.addAttribute("userTags", userTags);

            // Add the missing fields if they don't exist
            boolean updateNeeded = false;
            if (!userData.containsKey("bio")) {
                userData.put("bio", null);
                updateNeeded = true;
            }
            if (!userData.containsKey("instagram")) {
                userData.put("instagram", null);
                updateNeeded = true;
            }
            if (!userData.containsKey("facebook")) {
                userData.put("facebook", null);
                updateNeeded = true;
            }
            if (!userData.containsKey("tag")) {
                userData.put("tag", new ArrayList<String>());
                updateNeeded = true;
            }
            // If any fields were missing, update the document in Firebase
            if (updateNeeded) {
                documentSnapshot.getReference().set(userData);
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return "viewprofile";
    }



    @PostMapping("/updateUserProfile")
    @ResponseBody
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