package com.fawwaz_bank.bill_splitter.controller;

import com.fawwaz_bank.bill_splitter.model.Payment;
import com.fawwaz_bank.bill_splitter.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense-payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(
            PaymentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return service.getAllPayments();
    }

    @PostMapping
    public Payment addPayment(
            @RequestBody Payment payment) {

        return service.addPayment(payment);
    }
}