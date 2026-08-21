package com.fawwaz_bank.bill_splitter.service;

import com.fawwaz_bank.bill_splitter.model.GroupMember;
import com.fawwaz_bank.bill_splitter.repository.GroupMemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupMemberService {

    private final GroupMemberRepository repository;

    public GroupMemberService(GroupMemberRepository repository) {
        this.repository = repository;
    }

    public List<GroupMember> getAllMembers() {
        return repository.findAll();
    }

    public GroupMember addMember(GroupMember member) {
        return repository.save(member);
    }
}