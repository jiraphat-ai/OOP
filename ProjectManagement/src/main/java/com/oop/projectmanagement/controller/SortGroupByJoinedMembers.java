package com.oop.projectmanagement.controller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


public class SortGroupByJoinedMembers extends SortGroup {


    public List<Map<String, Object>> sortGroup()
    {

        List<Map<String, Object>> sortedGroups = new ArrayList<>(groupSearchResult);
        Collections.sort(sortedGroups, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> group1, Map<String, Object> group2) {
                return group1.get("joinedMember").toString().compareTo(group2.get("joinedMember").toString());
            }
        });
        return sortedGroups;
    }


}
