package com.fawwaz_bank.bill_splitter.service;

import com.fawwaz_bank.bill_splitter.dto.SettlementResponse;
import com.fawwaz_bank.bill_splitter.model.*;
import com.fawwaz_bank.bill_splitter.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final SplitResultRepository splitResultRepository;
    private final PaymentRepository paymentRepository;
    private final ExpenseRepository expenseRepository;
    private final BillGroupRepository billGroupRepository;

    // GITHUB USERNAME
    private static final String GITHUB_USERNAME = "qfwz";

    public SettlementService(
            SettlementRepository settlementRepository,
            SplitResultRepository splitResultRepository,
            PaymentRepository paymentRepository,
            ExpenseRepository expenseRepository,
            BillGroupRepository billGroupRepository) {

        this.settlementRepository = settlementRepository;
        this.splitResultRepository = splitResultRepository;
        this.paymentRepository = paymentRepository;
        this.expenseRepository = expenseRepository;
        this.billGroupRepository = billGroupRepository;
    }

    public List<Settlement> getSettlements(Long groupId) {
        return settlementRepository.findByGroupId(groupId);
    }

    @Transactional
    public SettlementResponse generateSettlements(Long groupId) {

        BillGroup group =
                billGroupRepository.findById(groupId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Group not found"
                                ));

        settlementRepository.deleteByGroupId(groupId);

        List<Expense> expenses =
                expenseRepository.findByGroupId(groupId);

        Map<Long, BigDecimal> balances =
                new HashMap<>();

        BigDecimal totalExpenses = BigDecimal.ZERO;
        BigDecimal totalPayments = BigDecimal.ZERO;

        for (Expense expense : expenses) {

            totalExpenses =
                    totalExpenses.add(
                            expense.getAmount()
                    );

            List<SplitResult> splitResults =
                    splitResultRepository
                            .findByExpenseId(
                                    expense.getId()
                            );

            // Validate that all of the expense amount
            // has been assigned to split results
            BigDecimal totalSplit = BigDecimal.ZERO;

            for (SplitResult splitResult : splitResults) {

                totalSplit =
                        totalSplit.add(
                                splitResult.getShareAmount()
                        );
            }

            if (totalSplit.compareTo(expense.getAmount()) != 0) {

                throw new RuntimeException(
                        "Split results do not match expense amount "
                                + "for expense ID: "
                                + expense.getId()
                );
            }

            List<Payment> payments =
                    paymentRepository
                            .findByExpenseId(
                                    expense.getId()
                            );

            // Calculate user balances from split results
            for (SplitResult splitResult :
                    splitResults) {

                Long userId =
                        splitResult.getUser().getId();

                balances.put(
                        userId,
                        balances.getOrDefault(
                                userId,
                                BigDecimal.ZERO
                        ).subtract(
                                splitResult.getShareAmount()
                        )
                );
            }

            // Add payments to user balances
            for (Payment payment : payments) {

                Long userId =
                        payment.getUser().getId();

                BigDecimal amountPaid =
                        payment.getAmountPaid();

                totalPayments =
                        totalPayments.add(
                                amountPaid
                        );

                balances.put(
                        userId,
                        balances.getOrDefault(
                                userId,
                                BigDecimal.ZERO
                        ).add(
                                amountPaid
                        )
                );
            }
        }

        BigDecimal amountLeftToPay =
                totalExpenses.subtract(totalPayments);

        if (amountLeftToPay.compareTo(BigDecimal.ZERO) < 0) {
            amountLeftToPay = BigDecimal.ZERO;
        }

        // ==============================
        // SERVICE CHARGE
        // ==============================

        int serviceChargePct =
                calculateServiceChargePct();

        BigDecimal serviceChargeAmount =
                totalExpenses
                        .multiply(
                                BigDecimal.valueOf(
                                        serviceChargePct
                                )
                        )
                        .divide(
                                BigDecimal.valueOf(100)
                        );

        // ==============================
        // END SERVICE CHARGE
        // ==============================

        List<Long> debtors = new ArrayList<>();
        List<Long> creditors = new ArrayList<>();

        for (Map.Entry<Long, BigDecimal> entry :
                balances.entrySet()) {

            if (entry.getValue()
                    .compareTo(BigDecimal.ZERO) < 0) {

                debtors.add(entry.getKey());

            } else if (entry.getValue()
                    .compareTo(BigDecimal.ZERO) > 0) {

                creditors.add(entry.getKey());
            }
        }

        List<Settlement> settlements =
                new ArrayList<>();

        for (Long debtor : debtors) {

            BigDecimal debt =
                    balances.get(debtor).abs();

            for (Long creditor : creditors) {

                BigDecimal credit =
                        balances.get(creditor);

                if (debt.compareTo(BigDecimal.ZERO) == 0) {
                    break;
                }

                if (credit.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }

                BigDecimal amount =
                        debt.min(credit);

                Settlement settlement =
                        new Settlement(
                                group,
                                findUser(
                                        debtor,
                                        expenses
                                ),
                                findUser(
                                        creditor,
                                        expenses
                                ),
                                amount
                        );

                settlements.add(
                        settlementRepository.save(
                                settlement
                        )
                );

                debt =
                        debt.subtract(amount);

                balances.put(
                        creditor,
                        credit.subtract(amount)
                );
            }
        }

        return new SettlementResponse(
                settlements,
                amountLeftToPay,
                serviceChargePct,
                serviceChargeAmount
        );
    }

    private User findUser(
            Long userId,
            List<Expense> expenses) {

        for (Expense expense : expenses) {

            List<SplitResult> splitResults =
                    splitResultRepository
                            .findByExpenseId(
                                    expense.getId()
                            );

            for (SplitResult splitResult :
                    splitResults) {

                if (splitResult.getUser().getId()
                        .equals(userId)) {

                    return splitResult.getUser();
                }
            }
        }

        throw new RuntimeException(
                "User not found: " + userId
        );
    }

    // ==============================
    // SERVICE CHARGE CALCULATION
    // ==============================

    private int calculateServiceChargePct() {

        String username =
                GITHUB_USERNAME.toLowerCase();

        int sum = 0;

        for (char character : username.toCharArray()) {
            sum += character;
        }

        return sum % 10;
    }

    public SettlementResponse getSettlementResponse(Long groupId) {

        List<Settlement> settlements =
                settlementRepository.findByGroupId(groupId);

        List<Expense> expenses =
                expenseRepository.findByGroupId(groupId);

        BigDecimal totalExpense = BigDecimal.ZERO;
        BigDecimal totalPayment = BigDecimal.ZERO;

        for (Expense expense : expenses) {

            totalExpense =
                    totalExpense.add(
                            expense.getAmount()
                    );

            List<Payment> payments =
                    paymentRepository.findByExpenseId(
                            expense.getId()
                    );

            for (Payment payment : payments) {

                totalPayment =
                        totalPayment.add(
                                payment.getAmountPaid()
                        );
            }
        }

        BigDecimal amountLeftToPay =
                totalExpense.subtract(totalPayment);

        if (amountLeftToPay.compareTo(BigDecimal.ZERO) < 0) {
            amountLeftToPay = BigDecimal.ZERO;
        }

        // ==============================
        // SERVICE CHARGE
        // ==============================

        int serviceChargePct =
                calculateServiceChargePct();

        BigDecimal serviceChargeAmount =
                totalExpense
                        .multiply(
                                BigDecimal.valueOf(
                                        serviceChargePct
                                )
                        )
                        .divide(
                                BigDecimal.valueOf(100)
                        );

        // ==============================
        // END SERVICE CHARGE
        // ==============================

        return new SettlementResponse(
                settlements,
                amountLeftToPay,
                serviceChargePct,
                serviceChargeAmount
        );
    }
}