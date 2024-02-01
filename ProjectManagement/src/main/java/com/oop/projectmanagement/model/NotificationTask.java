package com.oop.projectmanagement.model;
import com.oop.projectmanagement.controller.CustomControl;


public class NotificationTask extends CustomControl {
    private String taskName;
    private String status;
    private String request_id;
    private String group_id;

    // no-arg constructor
    public NotificationTask() {}

    // getters and setters
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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