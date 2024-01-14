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

    @PostMapping("/addUser")
    public String addUser(
    @RequestParam("firstName") String firstName, // Get the value from the form
    @RequestParam("lastName") String lastName,
    @RequestParam("password") String password,
    @RequestParam("userType") String userType,
    @RequestParam("username") String username) {

    Firestore db = firebaseInitializer.getDb(); // Get the database

    Map<String, Object> user = new HashMap<>(); // Create a new map
    user.put("firstName", firstName); // Put the values into the map
    user.put("lastName", lastName);
    user.put("password", password);
    user.put("userType", userType);
    user.put("username", username);

    // Set regDate with a specific date and time
    SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy 'at' HH:mm:ss z");
    sdf.setTimeZone(TimeZone.getTimeZone("UTC+7")); // Set timezone as per your requirement

    try {
        Date date = sdf.parse("January 10, 2024 at 17:05:55 UTC+7");
        Timestamp timestamp = new Timestamp(date.getTime());
        user.put("regDate", timestamp);
    } catch (Exception e) {
        e.printStackTrace();
        return "error";
    }

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