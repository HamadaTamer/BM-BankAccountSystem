package com.example.demo.mapper;
import com.example.demo.dto.AccountDTO;
import com.example.demo.model.Account;

public final class BankAccountMapper {
    private BankAccountMapper() {}
    public static AccountDTO toDto(Account a) {
        Long cid = a.getCustomer() == null ? null : a.getCustomer().getId();
        return new AccountDTO(a.getId(), a.getAccountNumber(), a.getAccountType(), a.getBalance(), cid);
    }
}
