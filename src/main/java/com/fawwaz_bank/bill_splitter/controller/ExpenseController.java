package com.fawwaz_bank.bill_splitter.controller;

import com.fawwaz_bank.bill_splitter.dto.CreateExpenseRequest;
import com.fawwaz_bank.bill_splitter.model.BillGroup;
import com.fawwaz_bank.bill_splitter.model.Expense;
import com.fawwaz_bank.bill_splitter.service.BillGroupService;
import com.fawwaz_bank.bill_splitter.service.ExpenseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final BillGroupService billGroupService;

    public ExpenseController(
            ExpenseService expenseService,
            BillGroupService billGroupService) {

        this.expenseService = expenseService;
        this.billGroupService = billGroupService;
    }

    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @PostMapping
    public Expense addExpense(
            @RequestBody CreateExpenseRequest request) {

        BillGroup group =
                billGroupService.getGroupById(request.getGroupId());

        Expense expense = new Expense();

        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setGroup(group);

        return expenseService.createExpense(expense);
    }
}