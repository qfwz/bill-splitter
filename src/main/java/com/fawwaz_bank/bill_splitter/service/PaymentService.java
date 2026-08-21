package com.fawwaz_bank.bill_splitter.service;

import com.fawwaz_bank.bill_splitter.model.Expense;
import com.fawwaz_bank.bill_splitter.model.Payment;
import com.fawwaz_bank.bill_splitter.model.GroupMember;
import com.fawwaz_bank.bill_splitter.repository.PaymentRepository;
import com.fawwaz_bank.bill_splitter.repository.GroupMemberRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ExpenseService expenseService;

    public PaymentService(
            PaymentRepository paymentRepository,
            GroupMemberRepository groupMemberRepository,
            ExpenseService expenseService) {

        this.paymentRepository = paymentRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.expenseService = expenseService;
    }

    public List<Payment> getAllPayments() {

        System.out.println(
                "TOTAL PAYMENTS: " +
                        paymentRepository.count()
        );

        return paymentRepository.findAll();
    }

    public Payment addPayment(Payment payment) {

        // Ambil Expense lengkap dari database
        Expense expense =
                expenseService.getExpenseById(
                        payment.getExpense().getId()
                );

        if (payment.getAmountPaid() == null ||
                payment.getAmountPaid()
                        .compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero"
            );
        }

        List<GroupMember> members =
                groupMemberRepository.findByGroupId(
                        expense.getGroup().getId()
                );

        boolean isMember = members.stream()
                .anyMatch(member ->
                        member.getUser().getId()
                                .equals(payment.getUser().getId())
                );

        if (!isMember) {
            throw new IllegalArgumentException(
                    "User is not a member of this group"
            );
        }

        BigDecimal existingTotal =
                paymentRepository
                        .findByExpenseId(expense.getId())
                        .stream()
                        .map(Payment::getAmountPaid)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal newTotal =
                existingTotal.add(
                        payment.getAmountPaid()
                );

        if (newTotal.compareTo(expense.getAmount()) > 0) {
            throw new IllegalArgumentException(
                    "Total payments cannot exceed expense amount"
            );
        }

        payment.setExpense(expense);

        return paymentRepository.save(payment);
    }
}