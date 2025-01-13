package com.local.entity;

import com.local.util.password.PasswordConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class BankAccount extends AbstractEntity {
    @Column(unique = true, nullable = false, length = 16)
    @Pattern(regexp = "\\d{16}")
    private String number;

    @Column(nullable = false, length = 511)
    @Convert(converter = PasswordConverter.class)
    private String password;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal balance;

    public BankAccount() {}

    public BankAccount(String number, String password, LocalDate expiryDate) {
        this.number = number;
        this.password = password;
        this.expiryDate = expiryDate;
        this.balance = BigDecimal.ZERO;
    }

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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
