package com.oop.projectmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.oop.projectmanagement.FirebaseInitializer;
import com.google.cloud.firestore.WriteResult;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.oop.projectmanagement.FirebaseInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class homestaffController {

    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/homestaff")
    public String portfileUser(Model model) {
        Firestore db = firebaseInitializer.getDb();
        List<Map<String, Object>> users = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> query = db.collection("users").get();
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
    @RequestParam("studentId") String studentId,
    @RequestParam("password") String password,
    @RequestParam("firstName") String firstName,
    @RequestParam("lastName") String lastName) {

    Firestore db = firebaseInitializer.getDb(); // Use getDb() instead of getFirebase()

    Map<String, Object> user = new HashMap<>();
    user.put("studentId", studentId);
    user.put("password", password);
    user.put("firstName", firstName);
    user.put("lastName", lastName);

    try {
        ApiFuture<DocumentReference> addedDocRef = db.collection("users").add(user);
    } catch (Exception e) {
        e.printStackTrace();
        return "error";
    }

    return "redirect:/homestaff";

    }

    @DeleteMapping("/deleteUser")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteUser(@RequestParam("studentId") String studentId) {
        Firestore db = firebaseInitializer.getDb();
        Map<String, Object> response = new HashMap<>();

        // Print the studentId you're trying to delete
        System.out.println("Deleting user with studentId: " + studentId);

        // Query Firestore to get the document with the matching studentId
        ApiFuture<QuerySnapshot> query = db.collection("users").whereEqualTo("studentId", studentId).get();
        QuerySnapshot querySnapshot = null;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // If no document was found, return an error
        if (querySnapshot.getDocuments().isEmpty()) {
            response.put("success", false);
            response.put("message", "No user found with the provided studentId");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        // Get the document ID of the first (and should be only) document found
        String documentId = querySnapshot.getDocuments().get(0).getId();

        // Delete the document
        ApiFuture<WriteResult> writeResult = db.collection("users").document(documentId).delete();
        try {
            System.out.println("Update time : " + writeResult.get().getUpdateTime());
            response.put("success", true);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}