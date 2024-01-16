package com.oop.projectmanagement.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.oop.projectmanagement.model.Group;
import com.oop.projectmanagement.model.GroupFordetail;
import com.oop.projectmanagement.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpSession;

import com.oop.projectmanagement.FirebaseInitializer;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class GroupDetail extends CustomControl {

    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/groupdetail")
    public String getUserinfo(HttpSession session , @RequestParam String documentId, Model model) throws ExecutionException, InterruptedException {
        String username = (String) session.getAttribute("username");
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        System.out.println("documentId " + documentId);
        GroupFordetail group = getGroupDetail(documentId);

        model.addAttribute("group", group);
        // Now you can use the username, firstName, and lastName
        return "groupdetail";

    }



    //get user detail from firestore by reference feild


    @PostMapping("/deleteGroup")
    @ResponseBody
    public String deleteGroup(@RequestParam String groupName) {
        Firestore firestore = firebaseInitializer.getDb();
        DocumentReference groupRef = firestore.collection("group").document(groupName);

        try {
            // Delete the group document
            WriteResult writeResult = groupRef.delete().get();
            return "Group deleted successfully";
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "Error deleting group: " + e.getMessage();
        }
    }
}

