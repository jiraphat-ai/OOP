package com.oop.projectmanagement.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;




public class SortGroupByJoinedMembers extends SortGroup {

    @Override
    public List<Map<String, Object>> sortGroup() {
        
        Collections.sort(groupSearchResult, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> group1, Map<String, Object> group2) {
                int joinedMembers1 = (int) group1.get("joinedMember");
                int joinedMembers2 = (int) group2.get("joinedMember");
                
                return Integer.compare(joinedMembers1, joinedMembers2);
            }
        });
        
        return groupSearchResult;
    }


     
}
