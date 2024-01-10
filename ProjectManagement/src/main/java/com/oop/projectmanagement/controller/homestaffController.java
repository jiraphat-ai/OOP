package com.oop.projectmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.oop.projectmanagement.FirebaseInitializer;
import com.google.cloud.firestore.WriteResult;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.oop.projectmanagement.FirebaseInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class homestaffController {

    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/homestaff")
    public String portfileUser(Model model) {
        Firestore db = firebaseInitializer.getDb();
        List<Map<String, Object>> users = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> query = db.collection("useraccount").get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                users.add(document.getData());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        model.addAttribute("users", users);
        return "homestaff";
    }

    @PostMapping("/addUser")
    public String addUser(
    @RequestParam("firstName") String firstName,
    @RequestParam("lastName") String lastName,
    @RequestParam("password") String password,
    @RequestParam("userType") String userType,
    @RequestParam("username") String username) {

    Firestore db = firebaseInitializer.getDb();

    Map<String, Object> user = new HashMap<>();
    user.put("firstName", firstName);
    user.put("lastName", lastName);
    user.put("password", password);
    user.put("userType", userType);
    user.put("username", username);

    // Set regDate with a specific date and time
    SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy 'at' HH:mm:ss z");
    sdf.setTimeZone(TimeZone.getTimeZone("UTC+7")); // Set timezone as per your requirement

    try {
        Date date = sdf.parse("January 10, 2024 at 17:05:55 UTC+7");
        Timestamp timestamp = new Timestamp(date.getTime());
        user.put("regDate", timestamp);
    } catch (Exception e) {
        e.printStackTrace();
        return "error";
    }

    try {
        ApiFuture<DocumentReference> addedDocRef = db.collection("useraccount").add(user);
    } catch (Exception e) {
        e.printStackTrace();
        return "error";
    }

    return "redirect:/homestaff";
}

@GetMapping("/searchUsers")
@ResponseBody
public ResponseEntity<Map<String, Object>> searchUsers(
        @RequestParam(name = "username", required = false) String username,
        @RequestParam(name = "name", required = false) String name) {

    Firestore db = firebaseInitializer.getDb();
    List<Map<String, Object>> users = new ArrayList<>();

    try {
        // สร้าง query ด้วยเงื่อนไขที่ต้องการค้นหา
        com.google.cloud.firestore.Query query = db.collection("useraccount");

        if (username != null && !username.isEmpty()) {
            query = query.whereEqualTo("username", username);
        }

        if (name != null && !name.isEmpty()) {
            // ใช้ whereEqualTo() หลายครั้งเพื่อทำเงื่อนไข OR
            query = query.whereEqualTo("firstName", name).whereEqualTo("lastName", name);
        }

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
            users.add(document.getData());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("users", users);
        return ResponseEntity.ok(response);

    } catch (Exception e) {
        e.printStackTrace();
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Error searching users");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

}