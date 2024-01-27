package com.oop.projectmanagement.model;
import java.util.ArrayList;
import java.sql.Timestamp;
import com.oop.projectmanagement.controller.CustomControl;

public class Task extends CustomControl{
    private String taskName;
    private ArrayList<String> member;
    private String  deadline;
    private String description;
    private String status;
    private String documentId;

    
    public Task() {}

    public String getTaskName() {
        return taskName;
    }
    public ArrayList<String> getMember() {
        return member;
    }
    public String getDeadline() { // Update the return type of getDeadline()
        return deadline;
    }
    public String getDescription() {
        return description;
    }
    public String getStatus() {
        return status;
    }
    public String getDocumentId() {
        return documentId;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public void setMember(ArrayList<String> member) {
        this.member = member;
    }
    public void setDeadline(String deadline) { // Update the parameter type of setDeadline()
        this.deadline = deadline;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;


    }
}


