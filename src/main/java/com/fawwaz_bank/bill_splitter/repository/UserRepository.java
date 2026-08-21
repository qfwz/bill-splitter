package com.fawwaz_bank.bill_splitter.repository;

import com.fawwaz_bank.bill_splitter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}