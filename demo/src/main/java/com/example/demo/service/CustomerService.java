package com.example.demo.service;

import com.example.demo.controllers.CustomerController;
import com.example.demo.dto.CustomerCreateRequest;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CustomerService {

    private final CustomerRepository repo;

    public CustomerService(CustomerRepository repo){
        this.repo = repo;
    }
    private static CustomerDTO toDto(Customer c) {
        return new CustomerDTO(c.getId(), c.getName(), c.getEmail(), c.getPhone());
    }

    public CustomerDTO create(CustomerCreateRequest req) {
        if (repo.existsByEmail(req.email())) throw new IllegalArgumentException("Email already in use");
        if (repo.existsByPhone(req.phone())) throw new IllegalArgumentException("Phone already in use");

        Customer c = new Customer();
        c.setName(req.name());
        c.setEmail(req.email());
        c.setPhone(req.phone());

        Customer saved = repo.save(c);
        return toDto(saved);
    }

    public CustomerDTO getById(Long id) {
        Customer c = repo.findById(id).orElseThrow(() -> notFound(id));
        return toDto(c);
    }

    public Page<CustomerDTO> getAll(Pageable pageable) {
        return repo.findAll(pageable).map(CustomerService::toDto);
    }

    public CustomerDTO update(Long id, CustomerCreateRequest req) {
        Customer c = repo.findById(id).orElseThrow(() -> notFound(id));

        if (!c.getEmail().equalsIgnoreCase(req.email()) && repo.existsByEmail(req.email()))
            throw new IllegalArgumentException("Email already in use");
        if (!c.getPhone().equals(req.phone()) && repo.existsByPhone(req.phone()))
            throw new IllegalArgumentException("Phone already in use");

        c.setName(req.name());
        c.setEmail(req.email());
        c.setPhone(req.phone());

        return toDto(repo.save(c));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) throw notFound(id);
        try {
            repo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Customer has related records and cannot be deleted");
        }
    }

    private static NoSuchElementException notFound(Long id) {
        return new NoSuchElementException("Customer not found with id " + id);
    }
}
