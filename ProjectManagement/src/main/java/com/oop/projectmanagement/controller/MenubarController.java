package com.oop.projectmanagement.controller;

import java.util.*;
import java.util.concurrent.ExecutionException;
import javax.print.Doc;
import javax.servlet.http.HttpSession;

import com.oop.projectmanagement.model.NotificationTask;
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
        ArrayList<NotificationTask> taskNotificationList = getTaskNotifications(session);
        //loop for show data in notificationList3
        System.out.println("userId " + session.getAttribute("documentId"));
        model.addAttribute("taskNotificationList", taskNotificationList);
        Integer count_noti = notificationList2.size() + taskNotificationList.size();
        model.addAttribute("notificationList", notificationList2);
        model.addAttribute("count_noti", count_noti);

    
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
                System.out.println("notificationList " + notification.getRequest_id());
                System.out.println("notificationList " + notification.getGroup_id());
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
            System.out.println("group_id " + group_id);
            ApiFuture<QuerySnapshot> future = db.collection("group").document(group_id).collection("request").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                System.out.println("document " + document.getId() + " => " + document.getData());
                DocumentReference userRef = (DocumentReference) document.get("user");
                Map<String, Object> member = new HashMap<>();
                member.put("user", userRef);
                member.put("role", "member");
                db.collection("group").document(group_id).collection("member").document(request_id).set(member);
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
    public ArrayList<NotificationTask> getTaskNotifications(HttpSession session) throws ExecutionException, InterruptedException {
        ArrayList<NotificationTask> notificationList = new ArrayList<>();
        Firestore db = firebaseInitializer.getDb();
        //get all group
        ApiFuture<QuerySnapshot> future = db.collection("group").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            //check user in sub-collection member with docmentid is manager or not
            DocumentReference userRef = db.collection("group").document(document.getId()).collection("member").document((String) session.getAttribute("documentId"));
            ApiFuture<DocumentSnapshot> future2 = userRef.get();
            DocumentSnapshot document2 = future2.get();
            //if useref != null and role = manager
            if(document2.exists())
              if(Objects.equals(document2.getString("role"), "manager")){
                //get all task in group
                ApiFuture<QuerySnapshot> future3 = db.collection("group").document(document.getId()).collection("tasks").get();
                List<QueryDocumentSnapshot> documents3 = future3.get().getDocuments();
                for (QueryDocumentSnapshot document3 : documents3) {
                    //get all task that status = "pending"
                    if(Objects.equals(document3.getString("status").toLowerCase(), "checking")){
                        NotificationTask notification = new NotificationTask();
                        notification.setTaskName(document3.getString("taskName"));
                        notification.setStatus(document3.getString("status"));
                        notification.setRequest_id(document3.getId());
                        notification.setGroup_id(document.getId());
                        notificationList.add(notification);
                    }
                }
            }

            //get all task in group
        }

        //show data in notificationList
        for (NotificationTask notification : notificationList) {
            System.out.println("notificationList " + notification.getRequest_id());
            System.out.println("notificationList " + notification.getGroup_id());
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
