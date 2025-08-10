package com.example.demo.service;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.model.Transaction;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.AccountRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepo;
    private final AccountRepository accountRepo;

    public TransactionService(TransactionRepository transactionRepo, AccountRepository accountRepo) {
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
    }

    private static TransactionDTO toDto(Transaction t) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(t.getId());
        dto.setType(t.getType());
        dto.setAmount(t.getAmount());
        dto.setTimestamp(t.getTimestamp());
        dto.setBankAccountId(t.getBankAccount().getId());
        dto.setCustomerId(t.getCustomer().getId());
        return dto;
    }

    public List<TransactionDTO> getTransactionsByAccount(Long accountId) {
        // Verify that the account exists
        if (!accountRepo.existsById(accountId)) {
            throw new NoSuchElementException("Account not found with id " + accountId);
        }
        
        return transactionRepo.findByBankAccountId(accountId)
                .stream()
                .map(TransactionService::toDto)
                .toList();
    }

    public void delete(Long transactionId) {
        if (!transactionRepo.existsById(transactionId)) {
            throw new NoSuchElementException("Transaction not found with id " + transactionId);
        }
        
        try {
            transactionRepo.deleteById(transactionId);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Transaction has related records and cannot be deleted");
        }
    }
}
