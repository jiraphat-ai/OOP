package com.oop.projectmanagement.model;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

public class Group {
    private String subjectID;
    private String group_name;
    private Integer section;
    private String group_description;
    private Integer max_member;
    private ArrayList<String> tag;

    private String owner;

    private String documentId;

    // no-arg constructor

    public Group() {}

    // getters and setters

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

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
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

    public Integer getJoinedMember() {
        return 1;
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
}
