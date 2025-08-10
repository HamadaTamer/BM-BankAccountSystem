package com.example.bankapp.service;

import com.example.bankapp.dto.CustomerDTO;
import com.example.bankapp.entity.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CustomerMappingService {
    private final ModelMapper modelMapper;

    public CustomerMappingService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Marwa Yehia - Example mapping method
    public CustomerDTO convertToDTO(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }

    public Customer convertToEntity(CustomerDTO dto) {
        return modelMapper.map(dto, Customer.class);
    }
}
