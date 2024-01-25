package com.oop.projectmanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Member;
import java.util.Comparator;
import java.util.List;

public abstract class SortMember {
    protected final MemberService memberService;

    public SortMember (MemberService memberService) {
        this.memberService = memberService;
    }

    protected abstract Comparator<Member> getComparator();

    @GetMapping
    public List<Member> sort() {
        List<Member> Account = memberService.findAll();
        Account.sort(getComparator());
        return Account;
    }
}
