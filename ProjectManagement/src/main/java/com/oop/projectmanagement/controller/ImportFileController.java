package com.oop.projectmanagement.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpSession;

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
public class ImportFileController {

      @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/importuserfile")
public String homestudent(HttpSession session, Model model) {
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
    return "importuserfile"; // Return the name of the view
}
   


    @GetMapping("/importsubjectfile")
    public String importSubjectFile(HttpSession session, Model model) {
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
        return "importsubjectfile";
    }

    @GetMapping("/createsubject")
    public String createSubject(HttpSession session, Model model) {
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
            ApiFuture<QuerySnapshot> query = db.collection("subject").get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                users.add(document.getData());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        model.addAttribute("users", users);
        return "createsubject";
    }
    @PostMapping("/addsubject")
    public void addSubject(@RequestParam("subjectID") String subjectID, @RequestParam("subjectName") String subjectName,
            Model model , HttpSession session) {
        try {
            //add to firebase firestore 
            Firestore db = firebaseInitializer.getDb();
            DocumentReference docRef = db.collection("subject").document( subjectID);
            // Add document data  with id "alovelace" using a hashmap
            Map<String, Object> data = new HashMap<>();
            data.put("subjectID", subjectID);
            data.put("subjectName", subjectName);
            //asynchronously write data
            docRef.set(data);
            System.out.println("Data added successfully");

        } catch (Exception e) {
            // Handle any exceptions that occur during the process
            e.printStackTrace();
            // You can add custom error handling logic here
        }
    }
}
