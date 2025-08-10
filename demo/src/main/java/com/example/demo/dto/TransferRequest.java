package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransferRequest(
        @NotNull Long senderAccountId,
        @NotNull Long receiverAccountId,
        @Positive double amount
) {}
