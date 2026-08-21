package com.fawwaz_bank.bill_splitter.controller;

import com.fawwaz_bank.bill_splitter.model.SplitResult;
import com.fawwaz_bank.bill_splitter.service.SplitResultService;
import com.fawwaz_bank.bill_splitter.model.Expense;
import com.fawwaz_bank.bill_splitter.service.ExpenseService;
import com.fawwaz_bank.bill_splitter.dto.PercentageSplitRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/split-result")
public class SplitResultController {

    private final SplitResultService service;
    private final ExpenseService expenseService;

    public SplitResultController(
            SplitResultService service,
            ExpenseService expenseService) {

        this.service = service;
        this.expenseService = expenseService;
    }

    @GetMapping
    public List<SplitResult> getAllParticipants() {
        return service.getAllParticipants();
    }

    @PostMapping
    public SplitResult addParticipant(
            @RequestBody SplitResult participant) {

        return service.addParticipant(participant);
    }

    @PostMapping("/expense/{expenseId}/equal")
    public List<SplitResult> splitEqual(
            @PathVariable Long expenseId) {

        Expense expense =
                expenseService.getExpenseById(expenseId);

        return service.splitEqual(expense);
    }

    @PostMapping("/expense/{expenseId}/percentage")
    public List<SplitResult> splitPercentage(
            @PathVariable Long expenseId,
            @RequestBody PercentageSplitRequest request) {

        Expense expense =
                expenseService.getExpenseById(expenseId);

        return service.splitByPercentage(
                expense,
                request.getPercentages()
        );
    }
}