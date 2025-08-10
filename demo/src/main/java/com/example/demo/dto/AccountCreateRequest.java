package com.example.demo.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;

public record AccountCreateRequest(
        @Nullable String accountNumber,
        @NotBlank String accountType,
        @PositiveOrZero double initialBalance,
        @NotNull Long customerId
) {}
