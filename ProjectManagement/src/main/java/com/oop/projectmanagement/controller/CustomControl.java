package com.oop.projectmanagement.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.oop.projectmanagement.FirebaseInitializer;
import com.oop.projectmanagement.model.Group;
import com.oop.projectmanagement.model.GroupFordetail;
import com.oop.projectmanagement.model.Subject;
import com.oop.projectmanagement.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class CustomControl {
    @Autowired
    protected FirebaseInitializer firebaseInitializer = new FirebaseInitializer();
    //Get group detail from firestore by document id
    public GroupFordetail getGroupDetail(String documentId) {
        Firestore db = firebaseInitializer.getDb();
        DocumentReference docRef = db.collection("group").document(documentId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = null;
        try {
            document = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        GroupFordetail group = null;
        if (document.exists()) {
            group = document.toObject(GroupFordetail.class);
            group.setDocumentId(documentId);
            return group;
        } else {
            System.out.println("No such document!");
            return null;
        }
    }
    public ArrayList<User> getAllUsersFromMembers(String docId ) {
        Firestore db = firebaseInitializer.getDb();
        ArrayList<User> userList = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> future = db.collection("group")
                    .document(docId)
                    .collection("member")
                    .get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                System.out.println("document " + document.getId() + " => " + document.getData());
                DocumentReference userRef = (DocumentReference) document.get("user");
                assert userRef != null;
                ApiFuture<DocumentSnapshot> userFuture = userRef.get();
                try {
                    DocumentSnapshot userDoc = userFuture.get();
                    if (userDoc.exists()) {
                        User user = userDoc.toObject(User.class);
                        userList.add(user);
                        assert user != null;
                        System.out.println("user " + user.getFirstName() + " " + user.getLastName());
                    }
                    else {
                        System.out.println("No such document!");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return userList;
    }

   protected String getDocumentRefSubjectFromSubjectID(String subjectID){
        Firestore db = firebaseInitializer.getDb();
        String documentRef = null;
        try {
            ApiFuture<QuerySnapshot> querySnapshot = db.collection("subject").whereEqualTo("subjectID", subjectID).get();
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                documentRef = document.getReference().getPath();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return documentRef;
    }

    


}
