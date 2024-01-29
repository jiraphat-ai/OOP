package com.oop.projectmanagement.controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.oop.projectmanagement.FirebaseInitializer;
import com.oop.projectmanagement.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class CustomControl {
    @Autowired
    protected FirebaseInitializer firebaseInitializer = new FirebaseInitializer();
    protected Firestore db = firebaseInitializer.getDb();
    //Get group detail from firestore by document id
    public GroupFordetail getGroupDetail(String documentId) {
         db = firebaseInitializer.getDb();
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
    public ArrayList<Member> getAllUsersFromMembers(String docId ) {
         db = firebaseInitializer.getDb();
        ArrayList<Member> userList = new ArrayList<>();
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
                        Member user = userDoc.toObject(Member.class);
                        //set document id
                        user.setDocumentId(document.getId());
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
         db = firebaseInitializer.getDb();
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

    public String getDocumentIdByDocRef(String docRef) throws ExecutionException, InterruptedException {
         db = firebaseInitializer.getDb();
        String documentId = "";
        DocumentSnapshot document = db.document(docRef).get().get();
        if (document.exists()) {
            documentId = document.getId();
        }
        return documentId;
    }

    public String getDocumentFeildByDocRef(DocumentReference docRef ,String field) throws ExecutionException, InterruptedException {
            db = firebaseInitializer.getDb();
            String documentId = "";
            DocumentSnapshot document = docRef.get().get();
            if (document.exists()) {
                documentId = document.getString(field);
            }
       return documentId;
   }
    protected List<Map<String, Object>> getTags() {
        Firestore db = firebaseInitializer.getDb();
        List<Map<String, Object>> tags = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> querySnapshot = db.collection("tags").get();
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                tags.add(document.getData());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return tags;
    }
}
