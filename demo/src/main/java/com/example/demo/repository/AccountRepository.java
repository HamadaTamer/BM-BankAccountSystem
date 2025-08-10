package com.example.demo.repository;

import com.example.demo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account,Long> {

    public List<Account> findByAccountType(String accountType);
    public List<Account> findByBalanceGreaterThan(Double amount);
//    @Query("SELECT b FROM BankAccount b WHERE b.balance BETWEEN :min AND :max")
    public List<Account> findByBalanceBetween(Double min, Double max);
}
