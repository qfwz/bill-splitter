package com.fawwaz_bank.bill_splitter.controller;

import com.fawwaz_bank.bill_splitter.dto.CreatePaymentRequest;
import com.fawwaz_bank.bill_splitter.model.Expense;
import com.fawwaz_bank.bill_splitter.model.Payment;
import com.fawwaz_bank.bill_splitter.model.User;
import com.fawwaz_bank.bill_splitter.service.ExpenseService;
import com.fawwaz_bank.bill_splitter.service.PaymentService;
import com.fawwaz_bank.bill_splitter.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense-payments")
public class PaymentController {

    private final PaymentService service;
    private final ExpenseService expenseService;
    private final UserService userService;

    public PaymentController(
            PaymentService service,
            ExpenseService expenseService,
            UserService userService) {

        this.service = service;
        this.expenseService = expenseService;
        this.userService = userService;
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return service.getAllPayments();
    }

    @PostMapping
    public Payment addPayment(
            @RequestBody CreatePaymentRequest request) {

        Expense expense =
                expenseService.getExpenseById(request.getExpenseId());

        User user =
                userService.getUserById(request.getUserId());

        Payment payment = new Payment();

        payment.setExpense(expense);
        payment.setUser(user);
        payment.setAmountPaid(request.getAmountPaid());

        return service.addPayment(payment);
    }
}