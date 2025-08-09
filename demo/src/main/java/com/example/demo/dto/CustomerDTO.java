package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.NoArgsConstructor;

public class CustomerDTO {

    private Long id;

    @NotBlank
    private String name;

    private String phone;

    @Email
    private String email;

    public CustomerDTO(Long id, String name, String email, String phone) {
         this.id = id;
         this.name = name;
         this.email = email;
         this.phone= phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
