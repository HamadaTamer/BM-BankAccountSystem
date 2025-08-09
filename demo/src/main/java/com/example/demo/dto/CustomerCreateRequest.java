package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record CustomerCreateRequest(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank String phone

) {
}
