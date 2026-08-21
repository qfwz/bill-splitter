package com.fawwaz_bank.bill_splitter.dto;

import java.math.BigDecimal;
import java.util.Map;

public class PercentageSplitRequest {

    private Map<Long, BigDecimal> percentages;

    public Map<Long, BigDecimal> getPercentages() {
        return percentages;
    }

    public void setPercentages(Map<Long, BigDecimal> percentages) {
        this.percentages = percentages;
    }
}