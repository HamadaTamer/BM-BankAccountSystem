package com.example.demo.controller;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository repo) {
        this.customerRepository = repo;
    }

    // List all customers
    @GetMapping
    public List<CustomerDTO> list() {
        return customerRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Get by id
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getById(@PathVariable Long id) {
        return customerRepository.findById(id)
                .map(c -> ResponseEntity.ok(toDTO(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Create
    @PostMapping
    public ResponseEntity<CustomerDTO> create(@Valid @RequestBody CustomerDTO dto) {
        // If email exists, return 400
        if (dto.getEmail() != null && customerRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        Customer c = new Customer(dto.getName(), dto.getEmail(), dto.getPhone());
        Customer saved = customerRepository.save(c);
        return ResponseEntity.created(URI.create("/api/customers/" + saved.getId())).body(toDTO(saved));
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable Long id, @Valid @RequestBody CustomerDTO dto) {
        Optional<Customer> opt = customerRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Customer c = opt.get();
        c.setName(dto.getName());
        c.setEmail(dto.getEmail());
        c.setPhone(dto.getPhone());
        customerRepository.save(c);
        return ResponseEntity.ok(toDTO(c));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!customerRepository.existsById(id)) return ResponseEntity.notFound().build();
        customerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Helper
    private CustomerDTO toDTO(Customer c) {
        return new CustomerDTO(c.getId(), c.getName(), c.getPhone(), c.getEmail());
    }
}
