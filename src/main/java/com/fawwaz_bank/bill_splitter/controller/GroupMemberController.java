package com.fawwaz_bank.bill_splitter.controller;

import com.fawwaz_bank.bill_splitter.model.GroupMember;
import com.fawwaz_bank.bill_splitter.service.GroupMemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group-members")
public class GroupMemberController {

    private final GroupMemberService service;

    public GroupMemberController(GroupMemberService service) {
        this.service = service;
    }

    @GetMapping
    public List<GroupMember> getAllMembers() {
        return service.getAllMembers();
    }

    @PostMapping
    public GroupMember addMember(@RequestBody GroupMember member) {
        return service.addMember(member);
    }
}