package com.example.demo.repository;

import com.example.demo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByAccountType(String accountType);
    List<Account> findByBalanceGreaterThan(Double amount);

    @Query("select a from Account a where a.balance between :min and :max")
    List<Account> findAccountsInRange(@Param("min") Double min, @Param("max") Double max);

    @Modifying
    @Query("update Account a set a.balance = a.balance + :delta where a.id = :id")
    int incrementBalance(@Param("id") Long id, @Param("delta") double delta);
}

