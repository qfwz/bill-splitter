package com.fawwaz_bank.bill_splitter.controller;

import com.fawwaz_bank.bill_splitter.dto.CreateBillGroupRequest;
import com.fawwaz_bank.bill_splitter.model.BillGroup;
import com.fawwaz_bank.bill_splitter.service.BillGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class BillGroupController {

    private final BillGroupService billGroupService;

    public BillGroupController(BillGroupService billGroupService) {
        this.billGroupService = billGroupService;
    }

    @GetMapping
    public List<BillGroup> getAllGroups() {
        return billGroupService.getAllGroups();
    }

    @PostMapping
    public BillGroup createGroup(@RequestBody CreateBillGroupRequest group) {

        BillGroup billGroup = new BillGroup();
        billGroup.setName(group.getName());

        return billGroupService.createGroup(billGroup);
    }
}