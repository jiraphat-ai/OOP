package com.oop.projectmanagement.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.oop.projectmanagement.FirebaseInitializer;

@Controller
public class ImportFileController extends MenubarController {

      @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/importuserfile")
public String homestudent(HttpSession session, Model model) {
    String username = (String) session.getAttribute("username");
    Firestore db = firebaseInitializer.getDb();
    try {
        ApiFuture<QuerySnapshot> query = db.collection("useraccount").whereEqualTo("username", username).get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            // Fetch the values you need from the document
            String firstName = document.getString("firstname");
            String lastName = document.getString("lastname");
            // Add the values to the model
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            model.addAttribute("username", username);
        }
    } catch (Exception e) {
        model.addAttribute("error", "An error occurred: " + e.getMessage());
    }
    return "importuserfile"; // Return the name of the view
}
   


    @GetMapping("/importsubjectfile")
    public String portfileSubject(HttpSession session, Model model) {

        return "importsubjectfile";
    }
}
