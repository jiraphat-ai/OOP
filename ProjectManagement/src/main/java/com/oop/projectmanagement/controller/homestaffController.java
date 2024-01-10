package com.oop.projectmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.oop.projectmanagement.FirebaseInitializer;



@Controller
public class homestaffController {

    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/homestaff")
    public String portfileUser(Model model) {
        return "homestaff";
    }

    @PostMapping("/addUser")
    public String addUserToFirestore(@RequestParam String studentId, @RequestParam String password, @RequestParam String firstName, @RequestParam String lastName) {
    Firestore firestore = firebaseInitializer.getFirestoreInstance();
    DocumentReference docRef = firestore.collection("users").document(studentId);
    ApiFuture<WriteResult> result = docRef.set(new User(studentId, password, firstName, lastName));
    try {
        System.out.println("Update time : " + result.get().getUpdateTime());
    } catch (Exception e) {
        e.printStackTrace();
    }
    return "redirect:/homestaff";
}

// User class for storing user data
private static class User {
    private String studentId;
    private String password;
    private String firstName;
    private String lastName;

    public User(String studentId, String password, String firstName, String lastName) {
        this.studentId = studentId;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters and setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

@GetMapping("/checkConnection")
public String checkFirestoreConnection(Model model) {
    Firestore firestore = firebaseInitializer.getFirestoreInstance();
    ApiFuture<QuerySnapshot> query = firestore.collection("users").get();
    try {
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            System.out.println("User: " + document.getId());
        }
    } catch (Exception e) {
        e.printStackTrace();
        return "Error connecting to Firestore: " + e.getMessage();
    }
    return "Connection successful";
}
}

    
