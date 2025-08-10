package com.example.demo.dto;

import jakarta.validation.constraints.Positive;

public record MoneyRequest(
        @Positive double amount
) {}
