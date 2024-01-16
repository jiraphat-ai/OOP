package com.oop.projectmanagement.controller;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
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
        ArrayList<GroupFordetail> groupList = getGroupDataInFireStore();
        model.addAttribute("groupList", groupList);
        // Now you can use the username, firstName, and lastName
        return "mygroup";

    }

    public ArrayList<GroupFordetail> getGroupDataInFireStore() throws ExecutionException, InterruptedException {
        Firestore db = firebaseInitializer.getDb();
        ArrayList<GroupFordetail> groupList = new ArrayList<>();
        db.collection("group").get().get().getDocuments().forEach((QueryDocumentSnapshot document) -> {
            GroupFordetail group = document.toObject(GroupFordetail.class);
            groupList.add(group);
            //add document id to group object
            group.setDocumentId(document.getId());
        });
        return groupList;
    }
}