package com.oop.projectmanagement.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.oop.projectmanagement.model.Group;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import com.oop.projectmanagement.FirebaseInitializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
public class CreateGroup extends CustomControl {

    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/creategroup")

    public String getUserinfo(HttpSession session , Model model) {
        String username = (String) session.getAttribute("username");
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        model.addAttribute("tags", getTags());
        model.addAttribute("subjectid", getSubjectID());
        
        // Now you can use the username, firstName, and lastName
        return "creategroup";

    }

    //get Tags from database firestore

    private List<Map<String, Object>> getSubjectID() {
        Firestore db = firebaseInitializer.getDb();
        List<Map<String, Object>> subjectid = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> querySnapshot = db.collection("subject").get();
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                subjectid.add(document.getData());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return subjectid;
    }

    @PostMapping("/createGroup")
    @ResponseBody
    public String createGroup(@ModelAttribute Group group, HttpSession session) {


        try {
                Firestore db = firebaseInitializer.getDb();
                String username = (String) session.getAttribute("username");
                group.setGroupOwner(username);
                //add to group collection and update document in id to group object
                ApiFuture<DocumentReference> documentReference = db.collection("group").add(group);
                DocumentReference document = documentReference.get();
                group.setSubjectRef(db.document(getDocumentRefSubjectFromSubjectID(group.getSubjectID())));
                group.setDocumentId(document.getId());
                document.set(group);
                db.collection("group").document(document.getId()).collection("member")
                        .document(session.getAttribute("documentId").toString()).set(
                    Map.of(
                    "user", db.document("useraccount/" + session.getAttribute("documentId")), 
                    "role", "owner")        
                );

                System.out.println("Data added successfully");
            
            return "Group created successfully";
        } catch (Exception e) {
            return "Error creating group: " + e.getMessage();
        }
    }


}