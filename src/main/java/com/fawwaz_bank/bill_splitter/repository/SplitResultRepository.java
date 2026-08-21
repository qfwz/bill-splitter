package com.fawwaz_bank.bill_splitter.repository;

import com.fawwaz_bank.bill_splitter.model.SplitResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SplitResultRepository extends JpaRepository<SplitResult, Long> {
    void deleteByExpenseId(Long expenseId);
    List<SplitResult> findByExpenseId(Long expenseId);
}