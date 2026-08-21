package com.fawwaz_bank.bill_splitter.service;

import com.fawwaz_bank.bill_splitter.model.Expense;
import com.fawwaz_bank.bill_splitter.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    public List<Expense> getAllExpenses() {
        return repository.findAll();
    }

    public Expense createExpense(Expense expense) {
        return repository.save(expense);
    }

    public Expense getExpenseById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Expense not found"));
    }
}