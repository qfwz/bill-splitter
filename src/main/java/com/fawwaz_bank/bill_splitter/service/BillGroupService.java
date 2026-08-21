package com.fawwaz_bank.bill_splitter.service;

import com.fawwaz_bank.bill_splitter.model.BillGroup;
import com.fawwaz_bank.bill_splitter.repository.BillGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillGroupService {

    private final BillGroupRepository billGroupRepository;

    public BillGroupService(BillGroupRepository billGroupRepository) {
        this.billGroupRepository = billGroupRepository;
    }

    public List<BillGroup> getAllGroups() {
        return billGroupRepository.findAll();
    }

    public BillGroup createGroup(BillGroup group) {
        return billGroupRepository.save(group);
    }

    public BillGroup getGroupById(Long id) {
        return billGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));
    }
}