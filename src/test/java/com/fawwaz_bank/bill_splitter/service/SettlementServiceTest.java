package com.fawwaz_bank.bill_splitter.service;

import com.fawwaz_bank.bill_splitter.dto.SettlementResponse;
import com.fawwaz_bank.bill_splitter.model.*;
import com.fawwaz_bank.bill_splitter.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SettlementServiceTest {

    @Mock
    private SettlementRepository settlementRepository;

    @Mock
    private SplitResultRepository splitResultRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private BillGroupRepository billGroupRepository;

    @InjectMocks
    private SettlementService settlementService;


    @Test
    void twoMembersOnePayment() {

        // NEW GROUP
        BillGroup group = new BillGroup();
        group.setId(1L);
        group.setName("Pacaran");

        // 2 MEMBERS
        User kiki = new User();
        kiki.setId(1L);
        kiki.setUsername("kiki");

        User bouba = new User();
        bouba.setId(2L);
        bouba.setUsername("bouba");

        // TOTAL EXPENSE
        Expense expense = new Expense(
                "Brunch",
                new BigDecimal("58000"),
                group
        );

        // SHARE IS CALCULATED MANUALLY SINCE THIS IS ONLY A SETTLEMENT TEST
        // BOUBA IS ONLY PAYING FOR DRINKS (16.000)
        // ITS KIKI's TURN TO PAY FOR FOOD TODAY (42.000)
        SplitResult kikiSplit = new SplitResult(
                expense,
                kiki,
                new BigDecimal("16000")
        );

        SplitResult boubaSplit = new SplitResult(
                expense,
                bouba,
                new BigDecimal("42000")
        );

        // BOUBA HAS PAID ALL 58000, SO KIKI SHOULD OWE BOUBA FOR FOOD ONLY
        Payment boubaPayment = new Payment(
                expense,
                bouba,
                new BigDecimal("58000")
        );

        when(billGroupRepository.findById(1L))
                .thenReturn(Optional.of(group));

        when(expenseRepository.findByGroupId(1L))
                .thenReturn(List.of(expense));

        when(splitResultRepository.findByExpenseId(expense.getId()))
                .thenReturn(List.of(
                        kikiSplit,
                        boubaSplit
                ));

        when(paymentRepository.findByExpenseId(expense.getId()))
                .thenReturn(List.of(
                        boubaPayment));

        when(settlementRepository.save(any(Settlement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // RESPONSE
        SettlementResponse response =
                settlementService.generateSettlements(1L);

        // ASSERT
        assertNotNull(response);

        assertEquals(
                BigDecimal.ZERO,
                response.getAmountLeftToPay()
        );

        // SHOULD JUST BE 1 SETTLEMENT
        assertEquals(
                1,
                response.getSettlements().size()
        );

        // CHECK IF ACCURATE
        System.out.println("CASE 1: " + group.getName() + "\nAmount left to pay: " + response.getAmountLeftToPay());

        System.out.println("Settlements:");
        response.getSettlements().forEach(settlement -> {
            System.out.println(settlement);
        });
    }

    @Test
    void threeMembersTwoPayments() {

        // NEW GROUP
        BillGroup group = new BillGroup();
        group.setId(1L);
        group.setName("Trio Kwekwek");

        // 3 MEMBERS
        User fawwaz = new User();
        fawwaz.setId(1L);
        fawwaz.setUsername("fawwaz");

        User hammam = new User();
        hammam.setId(2L);
        hammam.setUsername("hammam");

        User monica = new User();
        monica.setId(3L);
        monica.setUsername("monica");

        // TOTAL EXPENSE
        Expense expense = new Expense(
                "Dinner",
                new BigDecimal("120000"),
                group
        );

        // SHARE IS CALCULATED MANUALLY SINCE THIS IS ONLY A SETTLEMENT TEST
        // THEY ATE THE SAME MEAL SO IT SHOULD BE EQUAL SPLIT
        // 120.000 / 3 = 40.000
        SplitResult fawwazSplit = new SplitResult(
                expense,
                fawwaz,
                new BigDecimal("40000")
        );

        SplitResult hammamSplit = new SplitResult(
                expense,
                hammam,
                new BigDecimal("40000")
        );

        SplitResult monicaSplit = new SplitResult(
                expense,
                monica,
                new BigDecimal("40000")
        );

        // FAWWAZ HAS PAID 100.000
        Payment fawwazPayment = new Payment(
                expense,
                fawwaz,
                new BigDecimal("100000")
        );

        // MONIC PAID THE REST 20.000
        Payment monicaPayment = new Payment(
                expense,
                monica,
                new BigDecimal("20000")
        );


        when(billGroupRepository.findById(1L))
                .thenReturn(Optional.of(group));

        when(expenseRepository.findByGroupId(1L))
                .thenReturn(List.of(expense));

        when(splitResultRepository.findByExpenseId(expense.getId()))
                .thenReturn(List.of(
                        fawwazSplit,
                        hammamSplit,
                        monicaSplit
                ));

        when(paymentRepository.findByExpenseId(expense.getId()))
                .thenReturn(List.of(
                        fawwazPayment,
                        monicaPayment));

        when(settlementRepository.save(any(Settlement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // RESPONSE
        SettlementResponse response =
                settlementService.generateSettlements(1L);

        // ASSERT
        assertNotNull(response);

        assertEquals(
                BigDecimal.ZERO,
                response.getAmountLeftToPay()
        );

        // THERE SOULD BE 2 settlements
        assertEquals(
                2,
                response.getSettlements().size()
        );

        // CHECK IF ACCURATE
        System.out.println("CASE 2: " + group.getName() + "\nAmount left to pay: " + response.getAmountLeftToPay());

        System.out.println("Settlements:");
        response.getSettlements().forEach(settlement -> {
            System.out.println(settlement);
        });
    }

    @Test
    void fiveMembersThreePayments() {

        // NEW GROUP
        BillGroup group = new BillGroup();
        group.setId(1L);
        group.setName("Five Friends");

        // 5 MEMBERS
        User zelda = new User();
        zelda.setId(1L);
        zelda.setUsername("zelda");

        User link = new User();
        link.setId(2L);
        link.setUsername("link");

        User ganon = new User();
        ganon.setId(3L);
        ganon.setUsername("ganon");

        User mario = new User();
        mario.setId(4L);
        mario.setUsername("mario");

        User luigi = new User();
        luigi.setId(5L);
        luigi.setUsername("luigi");

        // TOTAL EXPENSE
        Expense expense = new Expense(
                "Ganon's Apology Party",
                new BigDecimal("950000"),
                group
        );

        // SHARE IS CALCULATED MANUALLY SINCE THIS IS ONLY A SETTLEMENT TEST
        // GANON IS PAYING FOR HALF THE BILL (950.000 /2 = 475.000)
        // THE REST OF THE GANG IS PAYING EQUALLY ( 475.000 / 4 = 118.750)

        SplitResult ganonSplit = new SplitResult(
                expense,
                ganon,
                new BigDecimal("475000")
        );

        SplitResult zeldaSplit = new SplitResult(
                expense,
                zelda,
                new BigDecimal("118750")
        );

        SplitResult linkSplit = new SplitResult(
                expense,
                link,
                new BigDecimal("118750")
        );

        SplitResult marioSplit = new SplitResult(
                expense,
                mario,
                new BigDecimal("118750")
        );

        SplitResult luigiSplit = new SplitResult(
                expense,
                luigi,
                new BigDecimal("118750")
        );



        // MARIO PAID FOR FIRST BATCH 300.000
        Payment marioPayment = new Payment(
                expense,
                mario,
                new BigDecimal("300000")
        );

        // ZELDA PAID FOR SECOND BATCH 400.000
        Payment zeldaPayment = new Payment(
                expense,
                zelda,
                new BigDecimal("400000")
        );

        // GANON PAID FOR THE LAST BILL 250.000
        Payment ganonPayment = new Payment(
                expense,
                ganon,
                new BigDecimal("250000")
        );


        when(billGroupRepository.findById(1L))
                .thenReturn(Optional.of(group));

        when(expenseRepository.findByGroupId(1L))
                .thenReturn(List.of(expense));

        when(splitResultRepository.findByExpenseId(expense.getId()))
                .thenReturn(List.of(
                        ganonSplit,
                        zeldaSplit,
                        linkSplit,
                        marioSplit,
                        luigiSplit
                ));

        when(paymentRepository.findByExpenseId(expense.getId()))
                .thenReturn(List.of(
                        marioPayment,
                        zeldaPayment,
                        ganonPayment));

        when(settlementRepository.save(any(Settlement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // RESPONSE
        SettlementResponse response =
                settlementService.generateSettlements(1L);

        // ASSERT
        assertNotNull(response);

        assertEquals(
                BigDecimal.ZERO,
                response.getAmountLeftToPay()
        );

        assertEquals(
                4,
                response.getSettlements().size()
        );

        // CHECK IF ACCURATE
        System.out.println("CASE 3: " + group.getName() + "\nAmount left to pay: " + response.getAmountLeftToPay());

        System.out.println("Settlements:");
        response.getSettlements().forEach(settlement -> {
            System.out.println(settlement);
        });
    }
}