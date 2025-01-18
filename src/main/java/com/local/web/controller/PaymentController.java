package com.local.web.controller;

import com.local.dto.BankAccountDto;
import com.local.entity.UserType;
import com.local.exception.service.payment.*;
import com.local.service.PaymentService;
import com.local.web.auth.AuthRequired;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController()
public class PaymentController {
    private PaymentService paymentService;

    @Autowired
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @AuthRequired(UserType.ADMIN)
    @PostMapping("/accounts")
    public ResponseEntity<BankAccountDto> addBankAccount(@Valid @RequestBody BankAccountDto account) throws DuplicateBankAccountException {
        return ResponseEntity.ok(paymentService.addBankAccount(account));
    }

    @AuthRequired(UserType.ADMIN)
    @PostMapping("/accounts/{id}/balance/{amount}")
    public ResponseEntity<Void> addBalance(@PathVariable long id, @PathVariable BigDecimal amount) throws BankAccountNotFoundException {
        paymentService.addBalance(id, amount);
        return ResponseEntity.ok().build();
    }

    @AuthRequired(UserType.WEB_USER)
    @PostMapping("/user/pay")
    public ResponseEntity<Void> accountPay(@RequestAttribute long userId, @Valid @RequestBody BankAccountDto account) throws PendingPaymentNotFoundException, BankAccountNotFoundException, WrongBankAccountPasswordException, InsufficientBalanceException, PaymentInProgressException, ExpiredBankAccountException {
        paymentService.accountPay(userId, account);
        return ResponseEntity.ok().build();
    }
}
