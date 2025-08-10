package com.example.demo.mapper;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.model.Transaction;

public final class TransactionMapper {
    private TransactionMapper() {}

    public static TransactionDTO toDto(Transaction t) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(t.getId());
        dto.setType(t.getType());
        dto.setAmount(t.getAmount());
        dto.setTimestamp(t.getTimestamp());
        dto.setBankAccountId(t.getBankAccount().getId());
        dto.setCustomerId(t.getCustomer().getId());
        return dto;
    }

    public static Transaction toEntity(TransactionDTO dto) {
        Transaction t = new Transaction();
        t.setId(dto.getId());
        t.setType(dto.getType());
        t.setAmount(dto.getAmount());
        t.setTimestamp(dto.getTimestamp());
        // bankAccount & customer would need to be fetched separately in service
        return t;
    }
}
