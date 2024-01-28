package com.oop.projectmanagement.controller;

import com.oop.projectmanagement.model.GroupFordetail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class sortGroupByGroupNameZTOA extends SortGroup {

    public List<GroupFordetail> sortGroup()
    {
        
        List<GroupFordetail> sortedGroups = new ArrayList<>(groupSearchResult);
        Collections.sort(sortedGroups, new Comparator<GroupFordetail>() {
            @Override
            public int compare(GroupFordetail group1, GroupFordetail group2) {
                String groupName1 = (String) group1.getGroupName();
                String groupName2 = (String) group2.getGroupName();
                return groupName2.compareToIgnoreCase(groupName1);
            }
        });
        return sortedGroups;
    }

}
