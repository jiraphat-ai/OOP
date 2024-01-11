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
public class homestaffController {

    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/homestaff")
    public String portfileUser(HttpSession session, Model model) {
        System.out.println("User: " + session.getAttribute("username"));
    String username = (String) session.getAttribute("username");
    Firestore db = firebaseInitializer.getDb();
    try {
        ApiFuture<QuerySnapshot> query = db.collection("useraccount").whereEqualTo("username", username).get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            // Fetch the values you need from the document
            String firstName = document.getString("firstName");
            String lastName = document.getString("lastName");
            // Add the values to the model
            model.addAttribute("firstname", firstName);
            model.addAttribute("lastname", lastName);
            model.addAttribute("username", username);

            System.out.println("User: " + firstName + " " + lastName + " " + username);
        }
    } catch (Exception e) {
        model.addAttribute("error", "An error occurred: " + e.getMessage());
    }
        List<Map<String, Object>> users = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> query = db.collection("useraccount").get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
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
    @RequestParam("firstName") String firstName,
    @RequestParam("lastName") String lastName,
    @RequestParam("password") String password,
    @RequestParam("userType") String userType,
    @RequestParam("username") String username) {

    Firestore db = firebaseInitializer.getDb();

    Map<String, Object> user = new HashMap<>();
    user.put("firstName", firstName);
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