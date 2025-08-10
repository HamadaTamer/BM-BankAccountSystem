package com.example.demo.repository;

import com.example.demo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByBankAccountId(Long accountId);

    // requirement: custom delete using @Modifying
    @Modifying
    @Query("delete from Transaction t where t.bankAccount.id = :accountId")
    int deleteByAccountId(@Param("accountId") Long accountId);
}
