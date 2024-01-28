package com.oop.projectmanagement.controller;
import com.oop.projectmanagement.model.GroupFordetail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortGroupByJoinedMembers extends SortGroup {

    @Override
    public List<GroupFordetail> sortGroup() {
        List<GroupFordetail> sortedGroups = new ArrayList<>(groupSearchResult);
        Collections.sort(sortedGroups, new Comparator<GroupFordetail>() {
            @Override
            public int compare(GroupFordetail group1, GroupFordetail group2) {
                return Integer.compare(group1.getJoinedMember(), group2.getJoinedMember());
            }
        });
        return sortedGroups;
    }
}