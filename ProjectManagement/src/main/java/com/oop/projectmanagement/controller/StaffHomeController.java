package com.oop.projectmanagement.controller;

import com.oop.projectmanagement.model.User;
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
public class StaffHomeController {

    @Autowired
    private FirebaseInitializer firebaseInitializer;

    
    @GetMapping("/homestaff") // Map ONLY GET Requests
    public String getUserinfo(HttpSession session,Model model) { // Get the session
        Firestore db = firebaseInitializer.getDb(); // Get the database
        String username = (String) session.getAttribute("username"); // Get the username from the session
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        List<Map<String, Object>> users = new ArrayList<>();
        try {

            ApiFuture<QuerySnapshot> query = db.collection("useraccount").get(); // Get all the documents from the collection
            QuerySnapshot querySnapshot = query.get(); // Get the documents
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments(); // Get the documents as a list
            for (QueryDocumentSnapshot document : documents) { // Loop through the documents
                users.add(document.getData());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        model.addAttribute("users", users);
        return "homestaff";
    }

    
    @PostMapping("/sortUserBySelection")
    public String sortUserBySelection(@RequestParam String sortOption, Model model, HttpSession session) {
        sortMemberAtoZ memberAtoZ = new sortMemberAtoZ();
        sortMemberZtoA memberZtoA = new sortMemberZtoA();
        List<User> result;

        switch (sortOption) {
            case "memberAtoZ":
                memberAtoZ.setMember(session);
                result = memberAtoZ.sortMember();
                break; 
            case "memberZtoA":
                memberZtoA.setMember(session);
                result = memberZtoA.sortMember();
                break; 
            default:
                result = null;
                break;
        }
        model.addAttribute("users", result);
        return "/homestaff"; // return the name of your view
    }

    @PostMapping("/addUser")
    public String addUser(User user) {
        Firestore db = firebaseInitializer.getDb(); // Get the database
        try {
            ApiFuture<DocumentReference> addedDocRef = db.collection("useraccount").add(user);

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/homestaff";
    }

@PostMapping("/resetPassword")
@ResponseBody
public ResponseEntity<Map<String, Object>> resetPassword(
        @RequestParam("username") String username,
        @RequestParam("newPassword") String newPassword) {

    Firestore db = firebaseInitializer.getDb();
    //test merge

    try {
        // Check if the user exists
        ApiFuture<QuerySnapshot> query = db.collection("useraccount").whereEqualTo("username", username).get();
        QuerySnapshot querySnapshot = query.get();
        if (querySnapshot.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Update the password for the user
        DocumentReference userRef = querySnapshot.getDocuments().get(0).getReference();
        userRef.update("password", newPassword);

        return new ResponseEntity<>(Map.of("success", true), HttpStatus.OK);
    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(Map.of("success", false, "message", "Error resetting password"), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
    @PostMapping("/deleteUser")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteUser(@RequestBody Map<String, String> user) {
        Firestore db = firebaseInitializer.getDb();
        String username = user.get("username");

        try {
            // Check if the user exists
            ApiFuture<QuerySnapshot> query = db.collection("useraccount").whereEqualTo("username", username).get();
            QuerySnapshot querySnapshot = query.get();
            if (querySnapshot.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Delete the user
            DocumentReference userRef = querySnapshot.getDocuments().get(0).getReference();
            userRef.delete();

            return new ResponseEntity<>(Map.of("success", true), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Map.of("success", false, "message", "Error deleting user"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}