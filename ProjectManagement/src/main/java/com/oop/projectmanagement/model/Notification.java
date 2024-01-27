package com.oop.projectmanagement.model;
import javax.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import com.google.cloud.Timestamp;
import com.oop.projectmanagement.controller.CustomControl;


public class Notification extends CustomControl {
    private String username;
    private String subject_id;
    private String request_id;
    private String group_id;

    // no-arg constructor
    public Notification() {}

    // getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subjectID) {
        this.subject_id = subjectID;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String requestID) {
        this.request_id = requestID;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String groupID) {
        this.group_id = groupID;
    }
}