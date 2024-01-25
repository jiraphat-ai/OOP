package com.oop.projectmanagement.controller;

import com.oop.projectmanagement.model.Member;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortMemberAtoZ extends SortMember {

    public List<Member> sortMember() {
        List<Member> sortedMembers = new ArrayList<>(memberSearchResult);
        Collections.sort(sortedMembers, new Comparator<Member>() {
            @Override
            public int compare(Member member1, Member member2) {
                String memberName1 = member1.getName();
                String memberName2 = member2.getName();
                return memberName1.compareToIgnoreCase(memberName2);
            }
        });
        return sortedMembers;
    }
}
