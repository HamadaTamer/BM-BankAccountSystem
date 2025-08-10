package com.example.demo.mapper;

import com.example.demo.dto.CustomerDTO;
import com.example.demo.model.Customer;

public final class CustomerMapper {
    private CustomerMapper() {}

    public static CustomerDTO toDto(Customer c) {
        return new CustomerDTO(c.getId(), c.getName(), c.getEmail(), c.getPhone());
    }

    public static Customer toEntity(CustomerDTO dto) {
        Customer c = new Customer();
        c.setId(dto.getId());
        c.setName(dto.getName());
        c.setEmail(dto.getEmail());
        c.setPhone(dto.getPhone());
        return c;
    }
}
