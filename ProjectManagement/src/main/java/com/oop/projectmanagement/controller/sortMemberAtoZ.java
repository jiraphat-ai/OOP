package com.oop.projectmanagement.controller;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class sortMemberAtoZ extends SortMember {

    @Override
    public List<Map<String, Object>> sortMember() {
        List<Map<String, Object>> sortedUsers = new ArrayList<>(memberSearchResult);
        Collections.sort(sortedUsers , new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> user1, Map<String, Object> user2) {
                String username1 = (String) user1.get("username");
                String username2 = (String) user2.get("username");
                return username1.compareToIgnoreCase(username2);
            }
        }); 
        return sortedUsers;
    }
}
