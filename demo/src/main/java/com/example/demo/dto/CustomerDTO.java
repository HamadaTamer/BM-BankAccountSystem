package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CustomerDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Pattern(regexp="^$|^\\+?[0-9]{7,15}$", message="Phone must be empty or a valid number")
    private String phone;

    @Email(message = "Email must be valid")
    private String email;

    public CustomerDTO() {}

    public CustomerDTO(Long id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
