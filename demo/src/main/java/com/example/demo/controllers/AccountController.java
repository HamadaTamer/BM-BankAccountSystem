package com.example.demo.controllers;

import com.example.demo.dto.*;
import com.example.demo.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AccountDTO> create(@Valid @RequestBody AccountCreateRequest req) {
        AccountDTO created = service.create(req);
        return ResponseEntity.created(URI.create("/api/accounts/" + created.getId())).body(created);
    }

    @GetMapping("/{id}")
    public AccountDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public Page<AccountDTO> getAll(Pageable pageable) {
        return service.getAll(pageable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }



    // POST /api/accounts/{id}/deposit
    @PostMapping("/{id}/deposit")
    public AccountDTO deposit(@PathVariable Long id, @Valid @RequestBody MoneyRequest req) {
        return service.deposit(id, req);
    }

    // POST /api/accounts/{id}/withdraw
    @PostMapping("/{id}/withdraw")
    public AccountDTO withdraw(@PathVariable Long id, @Valid @RequestBody MoneyRequest req) {
        return service.withdraw(id, req);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequest req) {
        service.transfer(req);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/type/{type}")
    public List<AccountDTO> byType(@PathVariable String type) {
        return service.byType(type);
    }

}
