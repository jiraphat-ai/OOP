package com.oop.projectmanagement.controller;

import com.oop.projectmanagement.model.User;

import java.util.List;
import javax.servlet.http.HttpSession;

abstract public class sortMember {

    abstract public List<User> sortMember();

    public List<User> memberSearchResult;

    public List<User> setMember(HttpSession session) {
        memberSearchResult = (List<User>) session.getAttribute("lastsearchmember");
        return memberSearchResult;
    }
}
