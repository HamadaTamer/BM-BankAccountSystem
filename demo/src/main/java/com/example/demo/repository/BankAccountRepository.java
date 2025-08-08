package com.example.demo.repository;

import com.example.demo.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount,Long> {

    public List<BankAccount> findByAccountType(String accountType);
    public List<BankAccount> findByBalanceGreaterThan(Double amount);
    @Query("SELECT b FROM BankAccount b WHERE b.balance BETWEEN :min AND :max")
    public List<BankAccount> findAccountsInRange(@Param("min") Double min, @Param("max") Double max);
}
