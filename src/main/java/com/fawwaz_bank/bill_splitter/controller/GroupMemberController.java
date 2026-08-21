package com.fawwaz_bank.bill_splitter.controller;

import com.fawwaz_bank.bill_splitter.dto.AddGroupMemberRequest;
import com.fawwaz_bank.bill_splitter.model.BillGroup;
import com.fawwaz_bank.bill_splitter.model.GroupMember;
import com.fawwaz_bank.bill_splitter.model.User;
import com.fawwaz_bank.bill_splitter.service.BillGroupService;
import com.fawwaz_bank.bill_splitter.service.GroupMemberService;
import com.fawwaz_bank.bill_splitter.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group-members")
public class GroupMemberController {

    private final GroupMemberService service;
    private final BillGroupService billGroupService;
    private final UserService userService;

    public GroupMemberController(
            GroupMemberService service,
            BillGroupService billGroupService,
            UserService userService) {

        this.service = service;
        this.billGroupService = billGroupService;
        this.userService = userService;
    }

    @GetMapping
    public List<GroupMember> getAllMembers() {
        return service.getAllMembers();
    }

    @PostMapping
    public GroupMember addMember(
            @RequestBody AddGroupMemberRequest member) {

        BillGroup group =
                billGroupService.getGroupById(member.getGroupId());

        User user =
                userService.getUserById(member.getUserId());

        GroupMember request = new GroupMember();

        request.setGroup(group);
        request.setUser(user);

        return service.addMember(request);
    }
}