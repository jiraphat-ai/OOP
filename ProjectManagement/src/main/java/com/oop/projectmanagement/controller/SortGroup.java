package com.oop.projectmanagement.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;


abstract public class SortGroup {

    
    abstract public List<Map<String, Object>> sortGroup();

    public List<Map<String, Object>> groupSearchResult;
    



    public List<Map<String, Object>> setGroup(HttpSession session) {
        groupSearchResult = (List<Map<String, Object>>) session.getAttribute("lastsearchgroup");
        return groupSearchResult;    
    }
    
} 
