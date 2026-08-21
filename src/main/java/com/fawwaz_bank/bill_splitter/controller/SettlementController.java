package com.fawwaz_bank.bill_splitter.controller;

import com.fawwaz_bank.bill_splitter.dto.SettlementResponse;
import com.fawwaz_bank.bill_splitter.service.SettlementService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
public class SettlementController {

    private final SettlementService settlementService;

    public SettlementController(
            SettlementService settlementService) {

        this.settlementService = settlementService;
    }

    @GetMapping("/{groupId}/settlements")
    public SettlementResponse getSettlements(
            @PathVariable Long groupId) {

        return settlementService.getSettlementResponse(groupId);
    }

    @PostMapping("/{groupId}/settlements/generate")
    public SettlementResponse generateSettlements(
            @PathVariable Long groupId) {

        return settlementService.generateSettlements(groupId);
    }
}