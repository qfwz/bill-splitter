package com.fawwaz_bank.bill_splitter.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    private BillGroup group;



    public Expense() {
    }

    public Expense(String description, BigDecimal amount, BillGroup group) {
        this.description = description;
        this.amount = amount;
        this.group = group;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BillGroup getGroup() {
        return group;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setGroup(BillGroup group) {
        this.group = group;
    }
}