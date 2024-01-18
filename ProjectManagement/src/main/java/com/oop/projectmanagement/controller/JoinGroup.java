package com.oop.projectmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties.Reactive.Session;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpSession;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot; 
import com.google.cloud.firestore.QuerySnapshot;
import com.oop.projectmanagement.FirebaseInitializer;
import com.google.cloud.firestore.Query;


@Controller
public class JoinGroup {
    public List<Map<String, Object>> lastsearchgroup; 
    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/joingroup")
    public String getUserinfo(HttpSession session,Model model) {
        String username = (String) session.getAttribute("username");
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        model.addAttribute("subjectid", getSubjectID());
        // Now you can use the username, firstName, and lastName
        return "joingroup";
    }
    @GetMapping("/searchgroup")
    public String searchGroup(@RequestParam("subjectID") String subjectID, @RequestParam("section") int section  ,  Model model) {
        List<Map<String, Object>> groups = getGroupsBySubjectId(subjectID , section);
        model.addAttribute("groups", groups);
        return "/joingroup";  // return the name of the view that will display the groups
    }
    public List<Map<String, Object>> getGroupsBySubjectId(String subjectID, int section ) {
        Firestore db = firebaseInitializer.getDb();
        List<Map<String, Object>> groups = new ArrayList<>();
        //i want to get the group that have the  same any text in subjectID and section
        try {
            Query query = db.collection("group").whereEqualTo("subjectID", subjectID);
            if (section != 0) {
                query = query.whereEqualTo("section", section);
            }

            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                groups.add(document.getData());
                lastsearchgroup = groups;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return groups;
    }
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
    public List<Map<String , Object>> getlastsearch(HttpSession session)  {
        session.setAttribute("lastsearchgroup", lastsearchgroup);
        return lastsearchgroup;
    }

    @PostMapping("/sortGroupBySelection")
    public String sortGroupBySelection(@RequestParam String sortOption, Model model, HttpSession session) {
        getlastsearch(session);
        sortGroupByGroupNameATOZ ATOZ = new sortGroupByGroupNameATOZ();
        sortGroupByGroupNameZTOA ZTOA = new sortGroupByGroupNameZTOA();
        //SortGroupByJoinedMembers sortGroupByJoinedMembers = new SortGroupByJoinedMembers();
        List<Map<String, Object>> result;

        switch (sortOption) {
            //case "joinedMembers":
              //  result = sortGroupByjoinedMember();
                //break; 
            case "groupNameAtoZ":
                ATOZ.setGroup(session);
                result = ATOZ.sortGroup();
                break; 
            case "groupNameZtoA":
                ZTOA.setGroup(session);
                result = ZTOA.sortGroup();
                break; 
            // add more cases as needed
            default:
                result = getlastsearch(session); // default case if the value doesn't match any known options
        }
        model.addAttribute("groups", result);
        return "/joingroup"; // return the name of your view
    }

// SORT BY NUMBER OF JOINED MEMBERS
    /*private List<Map<String, Object>> sortGroupByjoinedMember() {
        List<Map<String, Object>> sortedGroups = new ArrayList<>(lastsearchgroup);
        Collections.sort(sortedGroups, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> group1, Map<String, Object> group2) {
                int joinedMember1 =  (int) group1.get("joinedMember");
                int joinedMember2 =  (int) group2.get("joinedMember");
                return Integer.compare(joinedMember1, joinedMember2);
            }
        });
        return sortedGroups;
    }*/





}



