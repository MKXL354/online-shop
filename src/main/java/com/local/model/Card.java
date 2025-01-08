package com.local.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Card {
    private int id;
    private String number;
    private String password;
    private LocalDate expiryDate;
    private BigDecimal balance;

    public Card(int id, String number, String password, LocalDate expiryDate, BigDecimal balance) {
        this.id = id;
        this.number = number;
        this.password = password;
        this.expiryDate = expiryDate;
        this.balance = balance;
    }

    public Card(Card card){
        this.id = card.getId();
        this.number = card.getNumber();
        this.password = card.getPassword();
        this.expiryDate = card.getExpiryDate();
        this.balance = card.getBalance();
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
