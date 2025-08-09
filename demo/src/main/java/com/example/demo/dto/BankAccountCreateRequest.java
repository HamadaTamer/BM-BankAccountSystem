package com.example.demo.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record BankAccountCreateRequest(
        @NotBlank String accountType,
        @PositiveOrZero double initialBalance,
        @NotNull Long customerId
) {}
