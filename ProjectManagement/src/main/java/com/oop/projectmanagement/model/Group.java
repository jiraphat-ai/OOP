package com.oop.projectmanagement.model;

public class Group {
    private String groupID;
    private String subjectID;
    private String group_name;
    private String group_description;
    private String group_leader;

    // no-arg constructor
    public Group() {}

    // getters and setters

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getGroupName() {
        return group_name;
    }

    public void setGroupName(String group_name) {
        this.group_name = group_name;
    }

    public String getGroupDescription() {
        return group_description;
    }

    public void setGroupDescription(String group_description) {
        this.group_description = group_description;
    }

    public String getGroupLeader() {
        return group_leader;
    }

    public void setGroupLeader(String group_leader) {
        this.group_leader = group_leader;
    }

}
