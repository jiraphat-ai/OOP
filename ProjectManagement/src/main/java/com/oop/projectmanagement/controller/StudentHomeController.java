package com.oop.projectmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import javax.print.Doc;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.oop.projectmanagement.FirebaseInitializer;
import com.oop.projectmanagement.model.Task;
import java.util.List;



@Controller
public class StudentHomeController {

    @Autowired
    private FirebaseInitializer firebaseInitializer;




    // @GetMapping("/homestudent")
    // public String getUserinfo(HttpSession session,Model model) throws InterruptedException, ExecutionException  {
    //     String username = (String) session.getAttribute("username");
    //     String firstName = (String) session.getAttribute("firstName");
    //     String lastName = (String) session.getAttribute("lastName");
    //     return "homestudent";
    // }


// public String getUserTask(HttpSession session, Model model)throws InterruptedException, ExecutionException  {
//     String username = (String) session.getAttribute("username");
//     List<Task> taskList = new ArrayList<Task>();
//     Firestore db = firebaseInitializer.getDb();
//         CollectionReference groupCollectionReference = db.collection("group");
//         ApiFuture<QuerySnapshot> groupFuture = groupCollectionReference.get();

//         for (DocumentSnapshot groupDocument : groupFuture.get().getDocuments()) {
//             CollectionReference tasksCollectionReference = groupDocument.getReference().collection("tasks");
//             ApiFuture<QuerySnapshot> tasksFuture = tasksCollectionReference.get();

//             for (DocumentSnapshot taskDocument : tasksFuture.get().getDocuments()) {
//                 Task task = taskDocument.toObject(Task.class);
//                 taskList.add(task);

//             }
//         }
//         model.addAttribute("taskList", taskList);

//         return "homestudent";
//     }
@GetMapping("/homestudent")
    public String getUserinfo(HttpSession session,Model model) throws InterruptedException, ExecutionException  {

        String firstName = (String) session.getAttribute("firstName");
        String lastName = (String) session.getAttribute("lastName");
        String username = (String) session.getAttribute("username");
        Firestore db = firebaseInitializer.getDb();
        List<Task> taskList = new ArrayList<Task>();

        CollectionReference groupCollectionReference = db.collection("group");
        ApiFuture<QuerySnapshot> groupFuture = groupCollectionReference.get();

        for (DocumentSnapshot groupDocument : groupFuture.get().getDocuments()) {
            CollectionReference tasksCollectionReference = groupDocument.getReference().collection("tasks");
            ApiFuture<QuerySnapshot> tasksFuture = tasksCollectionReference.whereArrayContains("member", username).get();

            for (DocumentSnapshot taskDocument : tasksFuture.get().getDocuments()) {
                Task task = taskDocument.toObject(Task.class);
                taskList.add(task);
                System.out.println(task.getTaskName());
            }
        }

        model.addAttribute("taskList", taskList);

        return "homestudent";
    }
}
    



