package com.local.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class BankAccountDto {
    @Pattern(regexp = "\\d{16}", message = "invalid format. Expected format is 16 digits")
    @NotEmpty
    private String number;

    @Size(max = 64)
    @NotEmpty
    private String password;

    @NotEmpty
    private LocalDate expiryDate;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}
