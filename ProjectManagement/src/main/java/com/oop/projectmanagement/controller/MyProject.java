package com.oop.projectmanagement.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.oop.projectmanagement.model.GroupFordetail;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpSession;
import com.oop.projectmanagement.FirebaseInitializer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class MyProject {

    @Autowired
    private FirebaseInitializer firebaseInitializer;
String documentId;
    @GetMapping("/myproject")

    public String getUserinfo(HttpSession session , Model model) throws ExecutionException, InterruptedException {
        String username = (String) session.getAttribute("username");
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        System.out.println("documentId " + documentId);
        ArrayList<GroupFordetail> groupList = getUserGroupsByUsername(session.getAttribute("documentId").toString());
        model.addAttribute("groupList", groupList);
        // Now you can use the username, firstName, and lastName
        return "myproject";

    }
 public ArrayList<GroupFordetail> getUserGroupsByUsername(String documentId) throws ExecutionException, InterruptedException {
    Firestore db = firebaseInitializer.getDb();
    ArrayList<GroupFordetail> userGroups = new ArrayList<>();
    // Get all groups
    ApiFuture<QuerySnapshot> future = db.collection("group").get();
    List<QueryDocumentSnapshot> documents = future.get().getDocuments();
    for (QueryDocumentSnapshot document : documents) {
        // For each group, check if the user is in the 'member' sub-collection
        ApiFuture<QuerySnapshot> memberFuture = document.getReference().collection("member").get();
        List<QueryDocumentSnapshot> memberDocuments = memberFuture.get().getDocuments();
        for (QueryDocumentSnapshot memberDocument : memberDocuments) {
            //get field from member collection
            DocumentReference memberDocumentId = memberDocument.get("user" , DocumentReference.class);
            String memberDocumentIdString = memberDocumentId.getId();
            if (memberDocumentIdString.equals(documentId)) {
                GroupFordetail group = document.toObject(GroupFordetail.class);
                group.setDocumentId(document.getId());
                userGroups.add(group);
            }
        }
    }
    System.out.println("userGroups " + userGroups);
    return userGroups;
}

}
