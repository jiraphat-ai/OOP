package com.oop.projectmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpSession;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.oop.projectmanagement.FirebaseInitializer;
import com.oop.projectmanagement.model.GroupFordetail;

@Controller
public class ScrumBoardController extends CustomControl{

    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/scrum_board")

    public String getUserinfo(HttpSession session) {
        String username = (String) session.getAttribute("username");
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        // Now you can use the username, firstName, and lastName
        return "scrum_board";

    }
      @GetMapping("/groupscrumbord")
        public String getUserinfo(HttpSession session , @RequestParam String documentId, Model model) throws ExecutionException, InterruptedException {
        String username = (String) session.getAttribute("username");
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        System.out.println("documentId " + documentId);
        GroupFordetail group = getGroupDetail(documentId);
        model.addAttribute("groupDoc", documentId);
        model.addAttribute("group", group);
        // Now you can use the username, firstName, and lastName
        return "scrum_board";

    }
    //   //SHOW GROUP DETAIL SECTION
    // @GetMapping("/showGroupDetail")
    // public String getGroupMoreDetail(@RequestParam String documentId, Model model) {
    //     //System.out.println("documentId " + documentId);
    //     List<GroupFordetail> groupFordetail = searchDocumentById(documentId);
    //     model.addAttribute("GroupFordetail", groupFordetail);
    //     return "/GroupFordetailFragment";
    // }
    // // SEARCH GROUP DETAIL BY DOC_ID SECTION

    // public List<GroupFordetail> searchDocumentById(String documentId) {
    //     System.out.println("Searching document with ID: " + documentId);
    //     List<GroupFordetail> groupFordetailList = new ArrayList<>();
    //     Firestore db = firebaseInitializer.getDb();
    //     try {
    //         ApiFuture<QuerySnapshot> querySnapshot = db.collection("group").whereEqualTo("documentId", documentId).get();
    //         List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
    //         for (QueryDocumentSnapshot document : documents) {
    //             GroupFordetail groupFordetail = document.toObject(GroupFordetail.class);
    //             groupFordetail.setDocumentId(document.getId());
    //             groupFordetailList.add(groupFordetail);
    //         }
    //     } catch (InterruptedException | ExecutionException e) {
    //         e.printStackTrace();
    //     }
    //     System.out.println("Found " + groupFordetailList.size() + " documents");
    //     return groupFordetailList;
    // }





}
