package com.local.dto;

import com.local.entity.BankAccount;
import com.local.util.validation.ValidLocalDate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class BankAccountDto extends AbstractDto{
    @Pattern(regexp = "\\d{16}", message = "invalid format. Expected format is 16 digits")
    @NotEmpty
    private String number;

    @Size(max = 64)
    @NotEmpty
    private String password;

    @NotEmpty
    @ValidLocalDate
    private String expiryDate;

    public BankAccountDto(BankAccount bankAccount) {
        this.id = bankAccount.getId();
        this.number = bankAccount.getNumber();
        this.password = bankAccount.getPassword();
        this.expiryDate = bankAccount.getExpiryDate().toString();
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

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
