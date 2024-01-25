package com.oop.projectmanagement.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.oop.projectmanagement.FirebaseInitializer;
import com.oop.projectmanagement.model.Notification;

@Controller
public class MenubarController extends CustomControl {

    @Autowired
    private FirebaseInitializer firebaseInitializer;


    @GetMapping("/menu_bar_student")

    public String getBar(HttpSession session) throws ExecutionException, InterruptedException {
        String username = (String) session.getAttribute("username");
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        // Now you can use the username, firstName, and lastName
        //show data in notificationList
        ArrayList<Notification> notificationList2 = getRequest();
        
        //loop for show data in notificationList
        for (int i = 0; i < notificationList2.size(); i++) {
            System.out.println("notificationList2 " + notificationList2.get(i).getSubject_id());
            System.out.println("notificationList2 " + notificationList2.get(i).getUsername());
        }
        session.setAttribute("notificationList", notificationList2);

    
        return "menu_bar_student";

    }
    ArrayList<Notification> notificationList = new ArrayList<>();
    public ArrayList<Notification> getRequest() throws ExecutionException, InterruptedException {
         //get subject_id in collection group in frist loop  and username in sub-collection request in second loop
        db = firebaseInitializer.getDb();
        ApiFuture<QuerySnapshot> future = db.collection("group").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            System.out.println("document " + document.getId() + " => " + document.getData());
            String subject_id = document.getString("subjectID");
            ApiFuture<QuerySnapshot> future2 = db.collection("group").document(document.getId()).collection("request").get();
            List<QueryDocumentSnapshot> documents2 = future2.get().getDocuments();
            for (QueryDocumentSnapshot document2 : documents2) {
                System.out.println("document " + document2.getId() + " => " + document2.getData());
                DocumentReference userRef = (DocumentReference) document2.get("user");
                Notification notification = new Notification();
                notification.setSubject_id(subject_id);
                notification.setUsername(getDocumentFeildByDocRef(userRef, "username"));
                notificationList.add(notification);
            }
        }
       
        return notificationList;

    }

}
