package com.fawwaz_bank.bill_splitter.repository;

import com.fawwaz_bank.bill_splitter.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    List<Payment> findByExpenseId(Long expenseId);

    void deleteByExpenseId(Long expenseId);
}