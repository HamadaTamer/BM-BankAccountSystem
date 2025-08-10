package com.example.bankapp.dto;

import java.util.List;

public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
    private int age;
    private List<BankAccountDTO> accounts;

    public CustomerDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public List<BankAccountDTO> getAccounts() { return accounts; }
    public void setAccounts(List<BankAccountDTO> accounts) { this.accounts = accounts; }
}
