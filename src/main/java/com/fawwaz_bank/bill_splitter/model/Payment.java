package com.fawwaz_bank.bill_splitter.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "expense_id")
    private Expense expense;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amountPaid;

    public Payment() {
    }

    public Payment(
            Expense expense,
            User user,
            BigDecimal amountPaid) {

        this.expense = expense;
        this.user = user;
        this.amountPaid = amountPaid;
    }

    public Long getId() {
        return id;
    }

    public Expense getExpense() {
        return expense;
    }

    public User getUser() {
        return user;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }
}