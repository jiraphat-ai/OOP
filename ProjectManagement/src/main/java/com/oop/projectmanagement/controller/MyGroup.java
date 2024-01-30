package com.oop.projectmanagement.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.oop.projectmanagement.model.GroupFordetail;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpSession;
import com.oop.projectmanagement.FirebaseInitializer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Controller
public class MyGroup extends CustomControl{

    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/mygroup")
    public String getUserinfo(HttpSession session , Model model) throws ExecutionException, InterruptedException {
        String username = (String) session.getAttribute("username");
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        ArrayList<GroupFordetail> groupList = getUserGroupsByUsername(session.getAttribute("username").toString());
        model.addAttribute("groupList", groupList);
        // Now you can use the username, firstName, and lastName
        return "mygroup";

    }

    public ArrayList<GroupFordetail> getUserGroupsByUsername(String username) throws ExecutionException, InterruptedException {
        Firestore db = firebaseInitializer.getDb();
        ApiFuture<QuerySnapshot> future = db.collection("group").whereEqualTo("groupOwner", username).get();
        ArrayList<GroupFordetail> userGroups = new ArrayList<>();
        db.collection("group").whereEqualTo("groupOwner", username).get().get().getDocuments().forEach((QueryDocumentSnapshot document) -> {
            GroupFordetail group = document.toObject(GroupFordetail.class);
            group.setDocumentId(document.getId());
            userGroups.add(group);
        });
        return userGroups;
    }
}