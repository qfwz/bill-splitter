package com.fawwaz_bank.bill_splitter.service;

import com.fawwaz_bank.bill_splitter.model.Expense;
import com.fawwaz_bank.bill_splitter.model.SplitResult;
import com.fawwaz_bank.bill_splitter.model.GroupMember;
import com.fawwaz_bank.bill_splitter.repository.SplitResultRepository;
import com.fawwaz_bank.bill_splitter.repository.GroupMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SplitResultService {

    private final SplitResultRepository repository;
    private final GroupMemberRepository groupMemberRepository;

    public SplitResultService(
            SplitResultRepository repository,
            GroupMemberRepository groupMemberRepository) {

        this.repository = repository;
        this.groupMemberRepository = groupMemberRepository;
    }

    public List<SplitResult> getAllParticipants() {
        return repository.findAll();
    }

    public SplitResult addParticipant(
            SplitResult participant) {

        return repository.save(participant);
    }

    @Transactional
    public List<SplitResult> splitEqual(Expense expense) {

        clearExistingParticipants(expense.getId());

        List<GroupMember> members =
                groupMemberRepository.findByGroupId(
                        expense.getGroup().getId()
                );

        if (members.isEmpty()) {
            throw new IllegalArgumentException(
                    "Group has no members"
            );
        }

        BigDecimal shareAmount = expense.getAmount()
                .divide(
                        BigDecimal.valueOf(members.size()),
                        2,
                        RoundingMode.HALF_UP
                );

        List<SplitResult> participants =
                new ArrayList<>();

        for (GroupMember member : members) {

            SplitResult participant =
                    new SplitResult(
                            expense,
                            member.getUser(),
                            shareAmount
                    );

            participants.add(
                    repository.save(participant)
            );
        }

        return participants;
    }

    @Transactional
    public List<SplitResult> splitByPercentage(
            Expense expense,
            Map<Long, BigDecimal> percentages) {

        clearExistingParticipants(expense.getId());

        List<GroupMember> members =
                groupMemberRepository.findByGroupId(
                        expense.getGroup().getId()
                );

        if (members.isEmpty()) {
            throw new IllegalArgumentException(
                    "Group has no members"
            );
        }

        BigDecimal totalPercentage = percentages.values()
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPercentage.compareTo(BigDecimal.valueOf(100)) != 0) {
            throw new IllegalArgumentException(
                    "Percentages must add up to 100%"
            );
        }

        List<SplitResult> participants =
                new ArrayList<>();

        for (GroupMember member : members) {

            Long userId = member.getUser().getId();

            BigDecimal percentage = percentages.get(userId);

            if (percentage == null) {
                throw new IllegalArgumentException(
                        "Missing percentage for user ID: " + userId
                );
            }

            BigDecimal shareAmount =
                    expense.getAmount()
                            .multiply(percentage)
                            .divide(
                                    BigDecimal.valueOf(100),
                                    2,
                                    RoundingMode.HALF_UP
                            );

            SplitResult participant =
                    new SplitResult(
                            expense,
                            member.getUser(),
                            shareAmount
                    );

            participants.add(repository.save(participant));
        }

        return participants;
    }

    private void clearExistingParticipants(Long expenseId) {
        repository.deleteByExpenseId(expenseId);
    }
}