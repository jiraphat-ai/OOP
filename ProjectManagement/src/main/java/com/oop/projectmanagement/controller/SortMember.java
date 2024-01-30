package com.oop.projectmanagement.controller;

import com.oop.projectmanagement.model.User;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

abstract public class SortMember {

    abstract public List<Map<String, Object>> sortMember();

    public List<Map<String, Object>> memberSearchResult;

    public List<Map<String, Object>> setMember(HttpSession session) {
        memberSearchResult = (List<Map<String, Object>>) session.getAttribute("lastsearchmember");
        return memberSearchResult;
    }
}
