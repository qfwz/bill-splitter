package com.fawwaz_bank.bill_splitter.dto;

import java.math.BigDecimal;

public class CreateExpenseRequest {

    private String description;
    private BigDecimal amount;
    private Long groupId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}