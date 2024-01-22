package com.oop.projectmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpSession;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot; 
import com.google.cloud.firestore.QuerySnapshot;
import com.oop.projectmanagement.FirebaseInitializer;
import com.google.cloud.firestore.Query;



@Controller
public class JoinGroup extends CustomControl {
    public List<Map<String, Object>> lastsearchgroup; 
    @Autowired
    private FirebaseInitializer firebaseInitializer;


    @GetMapping("/joingroup")
    public String getUserinfo(HttpSession session, Model model) throws ExecutionException, InterruptedException {
        String username = (String) session.getAttribute("username");
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        model.addAttribute("subjectid", getSubjectID());

        // Now you can use the username, firstName, and lastName
        return "joingroup";

    }
    //SHOW GROUP DETAIL SECTION
    @GetMapping("/moregroupdetail")
    public String getGroupMoreDetail(@RequestParam String documentId, Model model) {
        //System.out.println("documentId " + documentId);
        List<Map<String, Object>> groupdetail = searchDocumentById(documentId);
        model.addAttribute("groupdetail", groupdetail);
        return "/groupDetailFragment";
    }


    @GetMapping("/searchgroup")
    public String searchGroup(@RequestParam("subjectID") String subjectID, @RequestParam("section") int section, @RequestParam(value = "tag", required = false) String[] tag, Model model) {
        List<String> tagList = tag != null ? Arrays.asList(tag) : null; // Convert the array to a list
        List<Map<String, Object>> groups = getGroupsBySubjectId(subjectID, section, tagList);
        //System.out.println("tag: " + tagList);
        for (Map<String, Object> group : groups) {
            //System.out.println("joinedMember: " + group.get("joinedMember"));
        }
        model.addAttribute("groups", groups);
        return "/joingroup"; // return the name of the view that will display the groups
    }
    public List<Map<String, Object>> getGroupsBySubjectId(String subjectID, int section, List<String> tag) {
        Firestore db = firebaseInitializer.getDb();
        List<Map<String, Object>> groups = new ArrayList<>();
        
        
        try {
            Query query = db.collection("group").whereEqualTo("subjectID", subjectID);
            
            if (section != 0) {
                query = query.whereEqualTo("section", section);
            }
            
            if (tag != null && !tag.isEmpty()) {
                List<QuerySnapshot> snapshots = new ArrayList<>();
                    for (String t : tag) {
                    Query tagQuery = query.whereArrayContains("tag", t);
                    //System.out.println("tagQuery: " + tagQuery);
                    snapshots.add(tagQuery.get().get());
                    }

                // Combine the results of the tag queries
                for (QuerySnapshot snapshot : snapshots) {
                    for (DocumentSnapshot document : snapshot.getDocuments()) {
                        Map<String, Object> groupData = document.getData();
                        groupData.put("documentId", document.getId());
                        groups.add(groupData);
                        //System.out.println("groupData: " + groupData);
                        lastsearchgroup = groups;
                        }
                    }      
                } else {
                ApiFuture<QuerySnapshot> querySnapshot = query.get();
                List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
            
                for (QueryDocumentSnapshot document : documents) {
                    Map<String, Object> groupData = document.getData();
                    groupData.put("documentId", document.getId());
                    groups.add(groupData);
                    //System.out.println("groupData: " + groupData);
                    lastsearchgroup = groups;
                    }           
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
        SortGroupByJoinedMembers JNM = new SortGroupByJoinedMembers();
        List<Map<String, Object>> result;

        switch (sortOption) {
            case "joinedMembers":
                JNM.setGroup(session);
                result = JNM.sortGroup();
                break;
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


// SEARCH GROUP DETAIL BY DOC_ID SECTION
  
public List<Map<String, Object>> searchDocumentById(String documentId) {
    System.out.println("Searching document with ID: " + documentId);
    List<Map<String, Object>> groupDetail = new ArrayList<>();
    Firestore db = firebaseInitializer.getDb();
    try {
        ApiFuture<QuerySnapshot> querySnapshot = db.collection("group").whereEqualTo("documentId", documentId).get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            Map<String, Object> groupDetailInfo = document.getData();
            groupDetailInfo.put("documentId", document.getId());
            groupDetail.add(groupDetailInfo);
        }
    } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
    }
    System.out.println("Found " + groupDetail.size() + " documents");
    return groupDetail;
}     

//test

}



