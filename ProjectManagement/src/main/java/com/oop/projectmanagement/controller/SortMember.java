package com.oop.projectmanagement.controller;

import com.oop.projectmanagement.model.Member;

import java.util.List;
import javax.servlet.http.HttpSession;

abstract public class SortMember {

    abstract public List<Member> sortMember();

    public List<Member> memberSearchResult;

    public List<Member> setMember(HttpSession session) {
        memberSearchResult = (List<Member>) session.getAttribute("lastsearchmember");
        return memberSearchResult;
    }
}