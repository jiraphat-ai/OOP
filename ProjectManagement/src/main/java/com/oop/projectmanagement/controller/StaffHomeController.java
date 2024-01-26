package com.oop.projectmanagement.controller;

import com.oop.projectmanagement.model.User;
import com.oop.projectmanagement.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.oop.projectmanagement.FirebaseInitializer;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpSession;
import com.google.api.core.ApiFuture;
import com.oop.projectmanagement.FirebaseInitializer;


@Controller
public class StaffHomeController {
    public List<User> lastsearchmember;
    @Autowired
    private FirebaseInitializer firebaseInitializer;

    
    @GetMapping("/homestaff") // Map ONLY GET Requests
    public String getUserinfo(HttpSession session,Model model) { // Get the session
        Firestore db = firebaseInitializer.getDb(); // Get the database
        String username = (String) session.getAttribute("username"); // Get the username from the session
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        List<Map<String, Object>> users = new ArrayList<>();
        try {

            ApiFuture<QuerySnapshot> query = db.collection("useraccount").get(); // Get all the documents from the collection
            QuerySnapshot querySnapshot = query.get(); // Get the documents
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments(); // Get the documents as a list
            for (QueryDocumentSnapshot document : documents) { // Loop through the documents
                users.add(document.getData());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        model.addAttribute("users", users);
        return "homestaff";
    }

    public List<User> getUserByUsername(String username) {
        Firestore db = firebaseInitializer.getDb();
        List<User> users = new ArrayList<>();

        try {
            Query query = db.collection("useraccount").whereEqualTo("username", username);
            List<QuerySnapshot> snapshots = new ArrayList<>();
                for (QuerySnapshot snapshot : snapshots) {
                    for (DocumentSnapshot document : snapshot.getDocuments()) {
                        User userData = document.toObject(User.class);
                        users.add(userData);
                        lastsearchmember = users;
                    }
                }
                ApiFuture<QuerySnapshot> querySnapshot = query.get();
                List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
                for (QueryDocumentSnapshot document : documents) {
                    User userData = document.toObject(User.class);
                    users.add(userData);
                    lastsearchmember = users;
              }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return users;

    }

    private List<Map<String, Object>> getUername() {
        Firestore db = firebaseInitializer.getDb();
        List<Map<String, Object>> username = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> querySnapshot = db.collection("useraccount").get();
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                username.add(document.getData());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return username;
    }

      public List<User> getlastsearch(HttpSession session)  {
        session.setAttribute("lastsearchmember", lastsearchmember);
        return lastsearchmember;
    }

    @PostMapping("/sortUserBySelection")
    public String sortUserBySelection(@RequestParam String sortOption, Model model, HttpSession session) {
        sortMemberAtoZ memberAtoZ = new sortMemberAtoZ();
        sortMemberZtoA memberZtoA = new sortMemberZtoA();
        List<User> result;

        switch (sortOption) {
            case "memberAtoZ":
                memberAtoZ.setMember(session);
                result = memberAtoZ.sortMember();
                break; 
            case "memberZtoA":
                memberZtoA.setMember(session);
                result = memberZtoA.sortMember();
                break; 
            default:
                result = getlastsearch(session);
        }
        model.addAttribute("users", result);
        return "/homestaff"; // return the name of your view
    }

    @PostMapping("/addUser")
    public String addUser(User user) {
        Firestore db = firebaseInitializer.getDb(); // Get the database
        try {
            ApiFuture<DocumentReference> addedDocRef = db.collection("useraccount").add(user);

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/homestaff";
    }

@PostMapping("/resetPassword")
@ResponseBody
public ResponseEntity<Map<String, Object>> resetPassword(
        @RequestParam("username") String username,
        @RequestParam("newPassword") String newPassword) {

    Firestore db = firebaseInitializer.getDb();
    //test merge

    try {
        // Check if the user exists
        ApiFuture<QuerySnapshot> query = db.collection("useraccount").whereEqualTo("username", username).get();
        QuerySnapshot querySnapshot = query.get();
        if (querySnapshot.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Update the password for the user
        DocumentReference userRef = querySnapshot.getDocuments().get(0).getReference();
        userRef.update("password", newPassword);

        return new ResponseEntity<>(Map.of("success", true), HttpStatus.OK);
    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(Map.of("success", false, "message", "Error resetting password"), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
    @PostMapping("/deleteUser")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteUser(@RequestBody Map<String, String> user) {
        Firestore db = firebaseInitializer.getDb();
        String username = user.get("username");

        try {
            // Check if the user exists
            ApiFuture<QuerySnapshot> query = db.collection("useraccount").whereEqualTo("username", username).get();
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
            return new ResponseEntity<>(Map.of("success", false, "message", "Error deleting user"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}