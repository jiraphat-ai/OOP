package com.oop.projectmanagement.controller;

import com.oop.projectmanagement.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.Map;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.oop.projectmanagement.FirebaseInitializer;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpSession;
import com.google.cloud.firestore.Query;


@Controller
public class StaffHomeController {
    public List<Map<String, Object>> lastsearchmember;
    @Autowired
    private FirebaseInitializer firebaseInitializer;

    
    @GetMapping("/homestaff") // Map ONLY GET Requests
    public String getUserinfo(HttpSession session,Model model) { // Get the session
        Firestore db = firebaseInitializer.getDb(); // Get the database
        String username = (String) session.getAttribute("username"); // Get the username from the session
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        // getalluser(session);
        List<Map<String, Object>> users = new ArrayList<>();
        try {

            ApiFuture<QuerySnapshot> query = db.collection("useraccount").get(); // Get all the documents from the collection
            QuerySnapshot querySnapshot = query.get(); // Get the documents
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments(); // Get the documents as a list
            for (QueryDocumentSnapshot document : documents) { // Loop through the documents
                users.add(document.getData());
                lastsearchmember = users;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        model.addAttribute("users", users);
        return "homestaff";
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

      public List<Map<String, Object>> getlastsearch(HttpSession session)  {
        session.setAttribute("lastsearchmember", lastsearchmember);
        return lastsearchmember;
    }

    @PostMapping("/sortUserBySelection")
    public String sortUserBySelection(@RequestParam String sortOption, Model model, HttpSession session) {
        sortMemberAtoZ memberAtoZ = new sortMemberAtoZ();
        sortMemberZtoA memberZtoA = new sortMemberZtoA();
        List<Map<String, Object>> result;

        switch (sortOption) {
            case "AtoZ":
                memberAtoZ.setMember(session);
                result = memberAtoZ.sortMember();
                break; 
            case "ZtoA":
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
  @PostMapping("/searchUser")
    @ResponseBody
    public List<Map<String, Object>> searchUser(@RequestBody Map<String, String> searchData) {
        String username = searchData.get("username");
        String name = searchData.get("name");

        Firestore db = firebaseInitializer.getDb();
        List<Map<String, Object>> result = new ArrayList<>();

        try {
            CollectionReference userCollection = db.collection("useraccount");

            Query query = userCollection;

            // ตรวจสอบว่ามีการส่งค่ามาหรือไม่ และทำการ filter ข้อมูลตามเงื่อนไข
            if (username != null && !username.isEmpty()) {
                query = query.whereEqualTo("username", username);
            }
            if (name != null && !name.isEmpty()) {
                query = query.whereEqualTo("firstName", name);
            }

            // ทำการ query ข้อมูลจาก Firestore
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

            // นำข้อมูลมาเก็บใน List<Map<String, Object>> สำหรับส่งกลับไปยัง HTML
            for (QueryDocumentSnapshot document : documents) {
                result.add(document.getData());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }

}