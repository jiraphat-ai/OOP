package com.oop.projectmanagement.model;
public class Subject {
    private String subjectID;
    private String subject_name;

    // no-arg constructor
    public Subject() {}

    // getters and setters
    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getSubjectName() {
        return subject_name;
    }

    public void setSubjectName(String subject_name) {
        this.subject_name = subject_name;
    }


}
