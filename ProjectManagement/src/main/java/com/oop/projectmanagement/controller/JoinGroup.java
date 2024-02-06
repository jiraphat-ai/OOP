package com.oop.projectmanagement.controller;

import com.google.cloud.firestore.*;
import com.oop.projectmanagement.model.GroupFordetail;
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
import com.oop.projectmanagement.FirebaseInitializer;


@Controller
public class JoinGroup extends CustomControl {
    public List<GroupFordetail> lastsearchgroup;
    
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



  @GetMapping("/searchgroup")
public String searchGroup(@RequestParam("subjectID") String subjectID, @RequestParam("section") String section, @RequestParam(value = "tag", required = false) String[] tag, Model model) {
    List<String> tagList = tag != null ? Arrays.asList(tag) : null; // Convert the array to a list
    List<GroupFordetail> groups = getGroupsBySubjectId(subjectID, section, tagList);
    model.addAttribute("groups", groups);
    return "/joingroup"; // return the name of the view that will display the groups
}
    public List<GroupFordetail> getGroupsBySubjectId(String subjectID, String section, List<String> tag) {
        Firestore db = firebaseInitializer.getDb(); // เชื่อมต่อกับ firebase
        List<GroupFordetail> groups = new ArrayList<>();
        if (db == null) {
            // Handle the case where db is null.
            return new ArrayList<>();
        }
        try {
            Query query = db.collection("group").whereEqualTo("subjectID", subjectID);
                query = query.whereEqualTo("section", section);
            

            if (tag != null && !tag.isEmpty()) {
                List<QuerySnapshot> snapshots = new ArrayList<>();
                for (String t : tag) {
                    Query tagQuery = query.whereArrayContains("tag", t);
                    snapshots.add(tagQuery.get().get());
                }

                for (QuerySnapshot snapshot : snapshots) {
                    for (DocumentSnapshot document : snapshot.getDocuments()) {
                        GroupFordetail groupData = document.toObject(GroupFordetail.class);
                        groups.add(groupData);
                        lastsearchgroup = groups;
                    }
                }
            } else {
                ApiFuture<QuerySnapshot> querySnapshot = query.get();
                List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

                for (QueryDocumentSnapshot document : documents) {
                    GroupFordetail groupData = document.toObject(GroupFordetail.class);
                    groups.add(groupData);
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
    public List<GroupFordetail> getlastsearch(HttpSession session)  {
        session.setAttribute("lastsearchgroup", lastsearchgroup);
        if (lastsearchgroup== null) {
            return new ArrayList<>();
        }
        return lastsearchgroup;
    }

    @PostMapping("/sortGroupBySelection")
    public String sortGroupBySelection(@RequestParam String sortOption, Model model, HttpSession session) {
        getlastsearch(session);
        sortGroupByGroupNameATOZ ATOZ = new sortGroupByGroupNameATOZ();
        sortGroupByGroupNameZTOA ZTOA = new sortGroupByGroupNameZTOA();
        SortGroupByJoinedMembers JNM = new SortGroupByJoinedMembers();
        List<GroupFordetail> result;

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

    public List<GroupFordetail> searchDocumentById(String documentId) {
        System.out.println("Searching document with ID: " + documentId);
        List<GroupFordetail> groupFordetailList = new ArrayList<>();
        Firestore db = firebaseInitializer.getDb();
        try {
            ApiFuture<QuerySnapshot> querySnapshot = db.collection("group").whereEqualTo("documentId", documentId).get();
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                GroupFordetail groupFordetail = document.toObject(GroupFordetail.class);
                groupFordetail.setDocumentId(document.getId());
                groupFordetailList.add(groupFordetail);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("Found " + groupFordetailList.size() + " documents");
        return groupFordetailList;
    }
    @PostMapping("/joinGroup")
    @ResponseBody
    public String joinGroup(@RequestParam String documentId, HttpSession session) {
        Firestore db = firebaseInitializer.getDb();
        System.out.println("Joining group with ID: " + documentId);
        try {
            if(session.getAttribute("documentId") == null) {
                return "Please login first";
            }
            else {
                DocumentSnapshot requestSnapshot = db.collection("group").document(documentId).collection("request").document(session.getAttribute("documentId").toString()).get().get();
                if(requestSnapshot.exists()) {
                    return "You have already sent a request to join this group";
                }
                DocumentSnapshot memberSnapshot = db.collection("group").document(documentId).collection("member").document(session.getAttribute("documentId").toString()).get().get();
                if(memberSnapshot.exists()) {
                    return "You are already a member of this group";
                }
                DocumentSnapshot groupSnapshot = db.collection("group").document(documentId).get().get();
                if(groupSnapshot.get("groupOwner").equals(session.getAttribute("username"))) {
                    return "You are the owner of this group";
                }
                 
                
                 //check all group is member aleary sent request same subject
                 ApiFuture<QuerySnapshot> querySnapshot = db.collection("group").whereEqualTo("subjectID", groupSnapshot.get("subjectID")).get();
                 List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
                 for (QueryDocumentSnapshot document : documents) {
                     //check all group is member aleary sent request same subject
                     DocumentReference docRef3 = db.collection("group").document(document.getId()).collection("request").document(session.getAttribute("documentId").toString());
                     if(docRef3.get().get().exists()){
                         return "You have already sent a request to subject in another group";
                     }
                 }
                db.collection("group").document(documentId).collection("request").document(session.getAttribute("documentId").toString()).set(
                            Map.of(
                                    "user", db.document("useraccount/" + session.getAttribute("documentId")),
                                    "role", "member")
                    );
                return "success";
                // Rest of your code
            }
        } catch (Exception e) {
            return "Error to join group: " + e.getMessage();
        }
    }

    @GetMapping("/loadstatus")
    @ResponseBody
    public String joingroup(@RequestParam String documentId, HttpSession session) {
        Firestore db = firebaseInitializer.getDb();
        System.out.println("Joining group with ID: " + documentId);
        try {
            if (session.getAttribute("documentId") == null) {
                return "Please login first";
            } else {
                DocumentSnapshot requestSnapshot = db.collection("group").document(documentId)
                        .collection("request").document(session.getAttribute("documentId").toString()).get().get();
                if (requestSnapshot.exists()) {
                    return "You have already sent a request to join this group";
                }
                DocumentSnapshot memberSnapshot = db.collection("group").document(documentId)
                        .collection("member").document(session.getAttribute("documentId").toString()).get().get();
                if (memberSnapshot.exists()) {
                    return "You are already a member of this group";
                }
                DocumentSnapshot groupSnapshot = db.collection("group").document(documentId).get().get();
                if (groupSnapshot.get("groupOwner").equals(session.getAttribute("username"))) {
                    return "You are the owner of this group";
                }
                

                
                return "success";
                // Rest of your code
            }
        } catch (Exception e) {
            return "Error to join group: " + e.getMessage();
        }
    }
    @PostMapping("/cancelRequest")
    @ResponseBody
    public String cancelRequest(@RequestParam String documentId, HttpSession session) {
        Firestore db = firebaseInitializer.getDb();
        System.out.println("Canceling request to join group with ID: " + documentId);
        try {
            if (session.getAttribute("documentId") == null) {
                return "Please login first";
            } else {
                // Check if the user has already sent a request
                DocumentSnapshot requestSnapshot = db.collection("group").document(documentId)
                        .collection("request").document(session.getAttribute("documentId").toString()).get().get();

                if (requestSnapshot.exists()) {
                    // Delete the request
                    db.collection("group").document(documentId).collection("request")
                            .document(session.getAttribute("documentId").toString()).delete();

                    return "success";
                } else {
                    return "You haven't sent a request to join this group";
                }
            }
        } catch (Exception e) {
            return "Error canceling join request: " + e.getMessage();
        }
    }

}



