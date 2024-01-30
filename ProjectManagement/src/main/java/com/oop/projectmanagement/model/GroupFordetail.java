package com.oop.projectmanagement.model;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.oop.projectmanagement.controller.CustomControl;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import javax.annotation.Nonnull;

public class GroupFordetail extends CustomControl {
    private String subjectID;
    private DocumentReference subjectRef;

    private String group_name;
    private String section;
    private String group_description;
    private Integer max_member;
    private ArrayList<String> tag;
   

    private String owner;

    private String documentId;

    private String docIdSubject;

    private Integer joined_member;



    // no-arg constructor

    public GroupFordetail() {}

    // getters and setters
    public void setSubjectRef(DocumentReference subjectRef) {
     
        this.subjectRef = subjectRef;
    }
    public DocumentReference getSubjectRef() {
       
        return subjectRef;
    }

    public void setSubjectID(String subjectID) {
     
        this.subjectID = subjectID;
    }
    public String getSubjectID() {
       
        return subjectID;
    }
    public void setJoinedMember(Integer joined_member) {
        this.joined_member = joined_member;
    }
    public Integer getJoinedMember() {
        return joined_member;
    }

    public String getGroupName() {
        return group_name;
    }

    public void setGroupName(String group_name) {
        this.group_name = group_name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getGroupDescription() {
        return group_description;
    }

    public void setGroupDescription(String group_description) {
        this.group_description = group_description;
    }

    public Integer getMaxMember() {
        return max_member;
    }

    public void setMaxMember(Integer max_member) {
        this.max_member = max_member;
    }

    public ArrayList<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }

 

    public String getGroupOwner() {
        return owner;
    }


    public void setGroupOwner(String username) {
        this.owner = username;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public ArrayList<Member> getMembers() {
        return this.documentId != null ? getAllUsersFromMembers(this.documentId) : null;
    }

    public Subject getSubject() throws Exception {
        // Assign the result to a DocumentReference variable
        Subject subject = subjectRef.get().get().toObject(Subject.class);
        return subject;
    }
}
