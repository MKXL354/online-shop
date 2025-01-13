package com.local.repository;

import com.local.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepo extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> findByNumber(String bankAccountNumber);
    Optional<BankAccount> findByNumberAndPassword(String bankAccountNumber, String password);
}
