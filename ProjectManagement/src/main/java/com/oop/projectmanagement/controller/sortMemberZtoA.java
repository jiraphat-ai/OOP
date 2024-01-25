package com.oop.projectmanagement.controller;

import com.oop.projectmanagement.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class sortMemberZtoA extends sortMember {

    @Override
    public List<User> sortMember() {
        List<User> sortedUsers = new ArrayList<>(memberSearchResult);
        Collections.sort(sortedUsers, Comparator.comparing(User::getUsername).reversed());
        return sortedUsers;
    }
}
