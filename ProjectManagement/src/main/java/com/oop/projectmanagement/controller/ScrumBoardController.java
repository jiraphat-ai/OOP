package com.oop.projectmanagement.controller;

import com.oop.projectmanagement.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.text.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpSession;

import com.google.api.Http;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.oop.projectmanagement.FirebaseInitializer;
import com.oop.projectmanagement.model.GroupFordetail;
import com.oop.projectmanagement.model.Task;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ScrumBoardController extends CustomControl {
    String username, firstName, lastName,
            documentId;
    @Autowired
    private FirebaseInitializer firebaseInitializer;

    @GetMapping("/scrum_board")

    public String getUserinfo(HttpSession session) {
        username = (String) session.getAttribute("username");
        firstName = (String) session.getAttribute("firstName");
        lastName = (String) session.getAttribute("lastName");
        // Now you can use the username, firstName, and lastName
        return "scrum_board";

    }

    @GetMapping("/groupscrumbord")
    public String getScrumboard(HttpSession session, @RequestParam String documentId, Model model)
            throws ExecutionException, InterruptedException {
        Firestore db = firebaseInitializer.getDb();
        
        String uid = (String) session.getAttribute("documentId");
        DocumentReference docRef = db.collection("group").document(documentId).collection("member").document(uid);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        String role = future.get().getString("role");
        System.out.println(role);
        if (role.equals("manager")) {
        username = (String) session.getAttribute("username");
        firstName = (String) session.getAttribute("firstName");
        lastName = (String) session.getAttribute("lastName");

        GroupFordetail group = getGroupDetail(documentId);
        model.addAttribute("groupDoc", documentId);
        model.addAttribute("group", group);
        ArrayList<Member> members = group.getMembers();
        model.addAttribute("members", members);
        // Now you can use the username, firstName, and lastName
        return "scrum_board";
        } else 
         {
        username = (String) session.getAttribute("username");
        firstName = (String) session.getAttribute("firstName");
        lastName = (String) session.getAttribute("lastName");
        GroupFordetail group = getGroupDetail(documentId);
        model.addAttribute("groupDoc", documentId);
        model.addAttribute("group", group);
        ArrayList<Member> members = group.getMembers();
        model.addAttribute("members", members);
            // Now you can use the username, firstName, and lastName
        return "memberscrum_board";
            }
        
    }
    @PostMapping("/createTask")
    @ResponseBody
    public String createTask(@RequestBody Task task) throws ParseException, InterruptedException, ExecutionException {
        System.out.println(task.getTaskName());
        System.out.println(task.getMember());
        System.out.println(task.getDeadline());
        System.out.println(task.getDescription());
        System.out.println(task.getDocumentId());

        Firestore db = firebaseInitializer.getDb();

        // Create a new document in the tasks collection
        Map<String, Object> taskData = new HashMap<>();
        taskData.put("taskName", task.getTaskName());
        taskData.put("description", task.getDescription());
        taskData.put("member", task.getMember());
        // Timestamp timestamp = convertStringToTimestamp(task.getDeadline());
        // taskData.put("deadline", timestamp);
        taskData.put("deadline", task.getDeadline());
        taskData.put("status", "To Do"); // Assuming the status is "To Do" when a task is created

        // Add the new task to the tasks collection
        ApiFuture<DocumentReference> addedDocRef = db.collection("group").document(task.getDocumentId())
                .collection("tasks").add(taskData);

        // Get the added task's ID
        String taskId = null;
        try {
            taskId = addedDocRef.get().getId();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "Error creating task";
        }

        // Add members to the member subcollection in the tasks document
        for (String member : task.getMember()) {
            // Get the user's document ID
            String userDocId = getUserDocumentId(member);

            // Create a reference to the useraccount document
            DocumentReference userRef = db.collection("useraccount").document(userDocId);

            // Create a map to store the user reference
            Map<String, Object> userRefMap = new HashMap<>();
            userRefMap.put("user", userRef);

            // Add the user reference to the member subcollection in the tasks document
            db.collection("group").document(task.getDocumentId()).collection("tasks").document(taskId)
                    .collection("member").document().set(userRefMap);
        }

        return "Task created successfully";
    }

    public String getUserDocumentId(String username) throws InterruptedException, ExecutionException {
        // Query the useraccount collection where the username field is equal to the
        // provided username
        ApiFuture<QuerySnapshot> querySnapshot = db.collection("useraccount").whereEqualTo("username", username).get();

        // Get the first document from the query results
        QueryDocumentSnapshot document = querySnapshot.get().getDocuments().get(0);

        // Return the document ID
        return document.getId();
    }

    @GetMapping("/getTasks")
    @ResponseBody
    public List<Task> getTasks(@RequestParam String status, @RequestParam String doc_Id)
            throws ExecutionException, InterruptedException {
        List<Task> tasks = new ArrayList<>();
        ApiFuture<QuerySnapshot> querySnapshot = db.collection("group").document(doc_Id).collection("tasks")
                .whereEqualTo("status", status).get();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Task task = document.toObject(Task.class);
            task.setDocumentId(document.getId()); // Set the document ID
            tasks.add(task);
        
        }
        return tasks;
    }

@PostMapping("changeState")
public ResponseEntity<Map<String, String>> changeTaskStatus(@RequestParam String status, @RequestParam String taskdoc_Id ,@RequestParam String documentId,HttpSession session) throws InterruptedException, ExecutionException {
    Firestore db = firebaseInitializer.getDb();

    // Update the task status in the tasks collection


    String uid = (String) session.getAttribute("documentId");
    String username = (String) session.getAttribute("username");
    DocumentReference docRef = db.collection("group").document(documentId).collection("member").document(uid);
    ApiFuture<DocumentSnapshot> future = docRef.get();

    // Retrieve the task from the Firestore database
    DocumentReference memRef = db.collection("group").document(documentId).collection("tasks").document(taskdoc_Id);
    ApiFuture<DocumentSnapshot> memfuture = memRef.get();
    DocumentSnapshot document = memfuture.get();
    List<String> members = (List<String>) document.get("member");
    String role = future.get().getString("role");
    if ((role.equals("manager")) || (members.contains(username))  ) {
        DocumentReference taskRef = db.collection("group").document(documentId).collection("tasks").document(taskdoc_Id);
        ApiFuture<WriteResult> updateResult = taskRef.update("status", status);
        updateResult.get();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Task status updated successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    } else {
        Map<String, String> response = new HashMap<>();
        response.put("message", "It's not your task,Not allow to change status");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
@PostMapping("/deleteTask")
public ResponseEntity<Map<String, String>> deleteTask(@RequestParam String taskdoc_Id, @RequestParam String documentId) throws InterruptedException, ExecutionException {
    Firestore db = firebaseInitializer.getDb();
    DocumentReference taskRef = db.collection("group").document(documentId).collection("tasks").document(taskdoc_Id);

    // Delete all documents in all subcollections of the task
    Iterable<CollectionReference> subcollections = taskRef.listCollections();
    for (CollectionReference subcollection : subcollections) {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = subcollection.get();
        List<QueryDocumentSnapshot> documents = querySnapshotApiFuture.get().getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            document.getReference().delete();
        }
    }

    // Delete the task itself
    ApiFuture<WriteResult> deleteResult = taskRef.delete();
    deleteResult.get();

    Map<String, String> response = new HashMap<>();
    response.put("message", "Task deleted successfully");

    return new ResponseEntity<>(response, HttpStatus.OK);
}





}