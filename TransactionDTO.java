package com.example.bankapp.dto;

import java.time.LocalDateTime;

public class TransactionDTO {
    private Long id;
    private Double amount;
    private String type;
    private LocalDateTime dateTime;

    public TransactionDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
}
