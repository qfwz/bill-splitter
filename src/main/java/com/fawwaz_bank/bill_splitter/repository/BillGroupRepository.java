package com.fawwaz_bank.bill_splitter.repository;

import com.fawwaz_bank.bill_splitter.model.BillGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillGroupRepository extends JpaRepository<BillGroup, Long> {
}