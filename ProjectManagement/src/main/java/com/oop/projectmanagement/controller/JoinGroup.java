package com.oop.projectmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpSession;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.oop.projectmanagement.FirebaseInitializer;

@Controller
public class JoinGroup {

    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/joingroup")
    public String getUserinfo(HttpSession session) {
        String username = (String) session.getAttribute("username");
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        // Now you can use the username, firstName, and lastName
        return "joingroup";
    }
    @GetMapping("/searchgroup")
    public String searchGroup(@RequestParam("subjectID") String subjectID, @RequestParam("section") String section  ,  Model model) {
        List<Map<String, Object>> groups = getGroupsBySubjectId(subjectID , section);
        model.addAttribute("groups", groups);
        return "/joingroup";  // return the name of the view that will display the groups
    }
    private List<Map<String, Object>> getGroupsBySubjectId(String subjectID, String section ) {
        Firestore db = firebaseInitializer.getDb();
        List<Map<String, Object>> groups = new ArrayList<>();
        //i want to get the group that have the  same any text in subjectID and section
        try {
            ApiFuture<QuerySnapshot> query = db.collection("group").whereEqualTo("subjectID", subjectID).whereEqualTo("section", section).get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                groups.add(document.getData());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }



        return groups;  }
}