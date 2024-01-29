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
import com.google.cloud.firestore.FieldValue;
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

    public String getBar(HttpSession session ,Model model) throws ExecutionException, InterruptedException {
        String username = (String) session.getAttribute("username");
        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        // Now you can use the username, firstName, and lastName
        //show data in notificationList
        ArrayList<Notification> notificationList2 = getRequest(session);
        
        //loop for show data in notificationList
        for (int i = 0; i < notificationList2.size(); i++) {
            System.out.println("notificationList2 " + notificationList2.get(i).getSubject_id());
            System.out.println("notificationList2 " + notificationList2.get(i).getUsername());
        }
        model.addAttribute("notificationList", notificationList2);

    
        return "menu_bar_student";

    }
    
    public ArrayList<Notification> getRequest(HttpSession session) throws ExecutionException, InterruptedException {
         //get subject_id in collection group in frist loop  and username in sub-collection request in second loop
         ArrayList<Notification> notificationList = new ArrayList<>();
         db = firebaseInitializer.getDb();
        ApiFuture<QuerySnapshot> future = db.collection("group").whereEqualTo("groupOwner", session.getAttribute("username")).get();
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
                notification.setRequest_id(document2.getId());
                notification.setGroup_id(document.getId());
                notificationList.add(notification);
            }
        }
       
        return notificationList;

    }


    @PostMapping("/rejectrequest")
    @ResponseBody
    public String rejectRequest(@RequestParam("request_id") String request_id ,@RequestParam("group_id") String group_id) {
        //delete request in sub-collection request
        db = firebaseInitializer.getDb();
        db.collection("group").document(group_id).collection("request").document(request_id).delete();
        return "success";
    }
    @PostMapping("/acceptrequest")
    @ResponseBody
    public String acceptRequest(@RequestParam("request_id") String request_id ,@RequestParam("group_id") String group_id) {
        try {
            //copy data in sub-collection request to sub-collection member
            db = firebaseInitializer.getDb();
            ApiFuture<QuerySnapshot> future = db.collection("group").document(group_id).collection("request").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                System.out.println("document " + document.getId() + " => " + document.getData());
                DocumentReference userRef = (DocumentReference) document.get("user");
                Map<String, Object> member = new HashMap<>();
                member.put("user", userRef);
                member.put("role", "member");
                db.collection("group").document(group_id).collection("member").add(member);
            }
            //delete request in sub-collection request
            db.collection("group").document(group_id).collection("request").document(request_id).delete();
            //update status in collection group
            DocumentReference groupRef = db.collection("group").document(group_id);
            //set field joinedMember = joinedMember + 1
            groupRef.update("joinedMember", FieldValue.increment(1));
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    // Manager notification
    public ArrayList<Notification> getTaskNotifications(HttpSession session) throws ExecutionException, InterruptedException {
        ArrayList<Notification> notificationList = new ArrayList<>();
        db = firebaseInitializer.getDb();
        ApiFuture<QuerySnapshot> future = db.collection("group").whereEqualTo("Manager", session.getAttribute("username")).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            ApiFuture<QuerySnapshot> future2 = db.collection("group").document(document.getId()).collection("tasks").whereEqualTo("status", "checking").get();
            List<QueryDocumentSnapshot> documents2 = future2.get().getDocuments();
            for (QueryDocumentSnapshot document2 : documents2) {
                DocumentReference userRef = (DocumentReference) document2.get("users");
                Notification notification = new Notification();
                notification.setSubject_id(document.getString("subjectID"));
                notification.setUsername(getDocumentFeildByDocRef(userRef, "username"));
                notification.setRequest_id(document2.getId());
                notification.setGroup_id(document.getId());
                notificationList.add(notification);
            }
        }
        return notificationList;
    }

    public void updateTaskStatus(String groupId, String taskId, String status) {
        DocumentReference taskRef = db.collection("group").document(groupId).collection("tasks").document(taskId);
        taskRef.update("status", status);
    }

    @PostMapping("/rejecttask")
    @ResponseBody
    public String rejectTask(@RequestParam("group_id") String group_id, @RequestParam("task_id") String task_id) {
        // Update status of the task to "doing"
        db = firebaseInitializer.getDb();
        DocumentReference taskRef = db.collection("group").document(group_id).collection("tasks").document(task_id);
        taskRef.update("status", "doing");
        return "success";
    }

    @PostMapping("/accepttask")
    @ResponseBody
    public String acceptTask(@RequestParam("group_id") String group_id, @RequestParam("task_id") String task_id) {
        // Update status of the task to "done"
        db = firebaseInitializer.getDb();
        DocumentReference taskRef = db.collection("group").document(group_id).collection("tasks").document(task_id);
        taskRef.update("status", "done");
        return "success";
    }

}
