package com.oop.projectmanagement.controller;

import com.google.cloud.firestore.*;
import com.oop.projectmanagement.model.GroupFordetail;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        model.addAttribute("tags", getTags());
        model.addAttribute("group", group);
        // Now you can use the username, firstName, and lastName
        return "groupdetail";

    }
 



    //get user detail from firestore by reference feild


    @PostMapping("/deleteGroup")
    @ResponseBody
    public String deleteGroup(@RequestParam String documentId) {
        Firestore firestore = firebaseInitializer.getDb();
        DocumentReference groupRef = firestore.collection("group").document(documentId);

        try {
            // Delete the group document
            WriteResult writeResult = groupRef.delete().get();
            return "Group deleted successfully";
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "Error deleting group: " + e.getMessage();
        }
    }

    @PostMapping("/editGroup")
    @ResponseBody
    public String editGroup(@RequestParam String documentId, @RequestParam String groupName,
                            @RequestParam String groupDescription) {
                        Firestore firestore = firebaseInitializer.getDb();
                        DocumentReference groupRef = firestore.collection("group").document(documentId);

                        try {
                            // Update the group document with the new information
                            groupRef.update("groupName", groupName, "groupDescription", groupDescription);
                            return "Group edited successfully";
                        } catch (Exception e) {
                            e.printStackTrace();
                            return "Error editing group: " + e.getMessage();
                        }
                    }

    @PostMapping("/setRole")
    @ResponseBody
    public String setRole(@RequestParam String documentId ,@RequestParam String groupId, @RequestParam String role) {
        Firestore firestore = firebaseInitializer.getDb();
        DocumentReference groupRef = firestore.collection("group").document(groupId);
        try {
            // Update the group document with the new information
            groupRef.collection("member").document(documentId).update("role", role);
            return "Role set successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error setting role: " + e.getMessage();
        }
    }
    @PostMapping("/deleteMember")
    @ResponseBody
    public String deleteMember(@RequestParam String documentId ,@RequestParam String groupId) {
        Firestore firestore = firebaseInitializer.getDb();
        DocumentReference groupRef = firestore.collection("group").document(groupId);
        try {
            // Update the group document with the new information
            groupRef.collection("member").document(documentId).delete();
            //update member count
            groupRef.update("joinedMember", FieldValue.increment(-1));
            return "Member deleted successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error deleting member: " + e.getMessage();
        }
    }
}

