package com.fawwaz_bank.bill_splitter.dto;

import java.math.BigDecimal;

public class CreateSplitResultRequest {

    private Long expenseId;
    private Long userId;
    private BigDecimal shareAmount;

    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(BigDecimal shareAmount) {
        this.shareAmount = shareAmount;
    }
}