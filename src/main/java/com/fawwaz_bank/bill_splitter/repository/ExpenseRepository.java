package com.fawwaz_bank.bill_splitter.repository;

import com.fawwaz_bank.bill_splitter.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByGroupId(Long groupId);
}