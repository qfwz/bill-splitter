package com.fawwaz_bank.bill_splitter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fawwaz_bank.bill_splitter.model.Settlement;

import java.math.BigDecimal;
import java.util.List;

public class SettlementResponse {

    private List<Settlement> settlements;
    private BigDecimal amountLeftToPay;

//    NGITUNG SERVICE CHARGE
    @JsonProperty("service_charge_pct")
    private int serviceChargePct;

    @JsonProperty("service_charge_amount")
    private BigDecimal serviceChargeAmount;

    public SettlementResponse(
            List<Settlement> settlements,
            BigDecimal amountLeftToPay,
//            PARAMETER SERVICE CHARGE
            int serviceChargePct,
            BigDecimal serviceChargeAmount) {

        this.settlements = settlements;
        this.amountLeftToPay = amountLeftToPay;
        this.serviceChargePct = serviceChargePct;
        this.serviceChargeAmount = serviceChargeAmount;
    }

    public List<Settlement> getSettlements() {
        return settlements;
    }

    public BigDecimal getAmountLeftToPay() {
        return amountLeftToPay;
    }

//    GETTER SERVICE CHARGE
    public int getServiceChargePct() {
        return serviceChargePct;
    }

    public BigDecimal getServiceChargeAmount() {
        return serviceChargeAmount;
    }
}