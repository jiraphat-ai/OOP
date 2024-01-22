package com.oop.projectmanagement.controller;

import com.oop.projectmanagement.model.GroupFordetail;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;


abstract public class SortGroup {

    
    abstract public List<GroupFordetail> sortGroup();

    public List<GroupFordetail> groupSearchResult;
    
    public List<GroupFordetail> setGroup(HttpSession session) {
        groupSearchResult = (List<GroupFordetail>) session.getAttribute("lastsearchgroup");
        return groupSearchResult;    
    }
    
} 
