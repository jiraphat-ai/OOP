package com.oop.projectmanagement.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpSession;

import com.oop.projectmanagement.model.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.oop.projectmanagement.FirebaseInitializer;

@Controller
public class ImportFileController {

    @Autowired
    private FirebaseInitializer firebaseInitializer;

        
    @GetMapping("/importuserfile")

    public String getUserinfo1(HttpSession session) {
        String username = (String) session.getAttribute("username");
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        // Now you can use the username, firstName, and lastName
        return "importuserfile"; 
       
    }

    @GetMapping("/importsubjectfile")
    public String getUserinfo(HttpSession session) {
        String username = (String) session.getAttribute("username");
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        // Now you can use the username, firstName, and lastName
        return "importsubjectfile";

    }

    @GetMapping("/createsubject")

    public String getUserinfo(HttpSession session,Model model ) {
        Firestore db = firebaseInitializer.getDb();
        String username = (String) session.getAttribute("username");
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        // Now you can use the username, firstName, and lastName
        
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
    public String addSubject(Subject subject) {
        try {
            // add to firebase firestore
            Firestore db = firebaseInitializer.getDb();
            DocumentReference docRef = db.collection("subject").document(subject.getSubjectID());
            docRef.set(subject);
            System.out.println("Data added successfully");

        } catch (Exception e) {
            // Handle any exceptions that occur during the process
            e.printStackTrace();
            // You can add custom error handling logic here
        }
        return "redirect:/createsubject";
    }


    @PostMapping("/deleteSubject")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteSubject(@RequestBody Map<String, String> user) {
        Firestore db = firebaseInitializer.getDb();
        String username = user.get("username");

        try {
            // Check if the user exists
            ApiFuture<QuerySnapshot> query = db.collection("subject").whereEqualTo("subjectID", username).get();
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
            return new ResponseEntity<>(Map.of("success", false, "message", "Error deleting user"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addNewTerm")
    @ResponseBody
    public String UpdateTerm (@RequestParam("term") String term) {
        Firestore db = firebaseInitializer.getDb();
        try {
            // add to firebase firestore
            DocumentReference docRef = db.collection("term").document("term");
            docRef.update("term", term);
            System.out.println("Data added successfully");
            return "success";
        } catch (Exception e) {
            // Handle any exceptions that occur during the process
            e.printStackTrace();
            return e.getMessage();
            // You can add custom error handling logic here
        }
        
    }
    
    @GetMapping("/getTerm") 
    @ResponseBody
    public String getTerm() {
        Firestore db = firebaseInitializer.getDb();
        String term = "";
        try {
            ApiFuture<QuerySnapshot> query = db.collection("term").get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            term = documents.get(0).get("term").toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return term;
    }
}
