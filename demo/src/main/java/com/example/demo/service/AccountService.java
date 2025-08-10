package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.mapper.BankAccountMapper;
import com.example.demo.model.Account;
import com.example.demo.model.Customer;
import com.example.demo.model.Transaction;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepo;
    private final CustomerRepository customerRepo;
    private final TransactionRepository txRepo;


    public AccountDTO create(AccountCreateRequest req) {
        Customer customer = customerRepo.findById(req.customerId())
                .orElseThrow(() -> new NoSuchElementException("customer not found with id " + req.customerId()));

        String accNum = (req.accountNumber() == null || req.accountNumber().isBlank())
                ? generateAccountNumber()
                : req.accountNumber();

        Account a = new Account();
        a.setAccountNumber(accNum);
        a.setAccountType(req.accountType());
        a.setBalance(req.initialBalance());
        a.setCustomer(customer);

        return BankAccountMapper.toDto(accountRepo.save(a));

//      Integrity violations check:
//        int tries = 0;
//        while (true) {
//            try {
//                return BankAccountMapper.toDto(accountRepo.save(a));
//            } catch (DataIntegrityViolationException e) {
//                if (++tries > 3) throw e;
//                a.setAccountNumber(generateAccountNumber());
//            }
//        }
    }


    public AccountDTO getById(Long id) {
        Account a = accountRepo.findById(id).orElseThrow(() -> nf(id));
        return BankAccountMapper.toDto(a);
    }


    public Page<AccountDTO> getAll(Pageable pageable) {
        return accountRepo.findAll(pageable).map(BankAccountMapper::toDto);
    }


    public void delete(Long id) {
        if (!accountRepo.existsById(id)) throw nf(id);
        accountRepo.deleteById(id);
    }


    @Transactional
    public AccountDTO deposit(Long accountId, MoneyRequest req) {
        Account a = accountRepo.findById(accountId).orElseThrow(() -> nf(accountId));
        a.setBalance(a.getBalance() + req.amount());
        Account saved = accountRepo.save(a);

        Transaction t = new Transaction();
        t.setType("DEPOSIT");
        t.setAmount(req.amount());
        t.setCustomer(a.getCustomer());
        t.setBankAccount(saved);
        txRepo.save(t);

        return BankAccountMapper.toDto(saved);
    }

    @Transactional
    public AccountDTO withdraw(Long accountId, MoneyRequest req) {
        Account a = accountRepo.findById(accountId).orElseThrow(() -> nf(accountId));
        if (a.getBalance() < req.amount()) throw new IllegalArgumentException("insufficient funds");
        a.setBalance(a.getBalance() - req.amount());
        Account saved = accountRepo.save(a);

        Transaction t = new Transaction();
        t.setType("WITHDRAW");
        t.setAmount(req.amount());
        t.setCustomer(a.getCustomer());
        t.setBankAccount(saved);
        txRepo.save(t);

        return BankAccountMapper.toDto(saved);
    }


    @Transactional
    public void transfer(TransferRequest req) {
        if (req.senderAccountId().equals(req.receiverAccountId()))
            throw new IllegalArgumentException("sender and receiver must differ");

        Account s = accountRepo.findById(req.senderAccountId()).orElseThrow(() -> nf(req.senderAccountId()));
        Account r = accountRepo.findById(req.receiverAccountId()).orElseThrow(() -> nf(req.receiverAccountId()));

        if (s.getBalance() < req.amount()) throw new IllegalArgumentException("insufficient funds");

        // debit
        s.setBalance(s.getBalance() - req.amount());
        accountRepo.save(s);
        Transaction ts = new Transaction();
        ts.setType("TRANSFER_OUT");
        ts.setAmount(req.amount());
        ts.setCustomer(s.getCustomer());
        ts.setBankAccount(s);
        txRepo.save(ts);

        // credit
        r.setBalance(r.getBalance() + req.amount());
        accountRepo.save(r);
        Transaction tr = new Transaction();
        tr.setType("TRANSFER_IN");
        tr.setAmount(req.amount());
        tr.setCustomer(r.getCustomer());
        tr.setBankAccount(r);
        txRepo.save(tr);
    }


    public List<AccountDTO> byType(String accountType) {
        return accountRepo.findByAccountType(accountType).stream().map(BankAccountMapper::toDto).toList();
    }


    public List<AccountDTO> balanceGreaterThan(double amount) {
        return accountRepo.findByBalanceGreaterThan(amount).stream().map(BankAccountMapper::toDto).toList();
    }


    public List<AccountDTO> range(double min, double max) {
        return accountRepo.findByBalanceBetween(min, max).stream().map(BankAccountMapper::toDto).toList();
    }

    private static final SecureRandom RND = new SecureRandom();
    private static String generateAccountNumber() {
        // simple 14-digit random
        StringBuilder sb = new StringBuilder(14);
        for (int i = 0; i < 14; i++) sb.append(RND.nextInt(10));
        return sb.toString();
    }

    private static NoSuchElementException nf(Long id) {
        return new NoSuchElementException("bank account not found with id " + id);
    }
}
