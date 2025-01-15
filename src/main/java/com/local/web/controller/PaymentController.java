package com.local.web.controller;

import com.local.dto.BankAccountDto;
import com.local.entity.UserType;
import com.local.exception.service.payment.*;
import com.local.service.PaymentService;
import com.local.web.auth.AuthRequired;
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
    @PostMapping("/account")
    public ResponseEntity<BankAccountDto> addBankAccount(@RequestBody BankAccountDto account) throws DuplicateBankAccountException {
        return ResponseEntity.ok(paymentService.addBankAccount(account));
    }

    @AuthRequired(UserType.ADMIN)
    @PostMapping("account/{id}/balance/{amount}")
    public ResponseEntity<Void> addBalance(@PathVariable long id, @PathVariable BigDecimal amount) throws BankAccountNotFoundException {
        paymentService.addBalance(id, amount);
        return ResponseEntity.ok().build();
    }

    @AuthRequired(UserType.WEB_USER)
    @PostMapping("user/pay")
    public ResponseEntity<Void> accountPay(@RequestAttribute long userId, @RequestBody BankAccountDto account) throws PendingPaymentNotFoundException, InvalidBankAccountException, InsufficientBalanceException, PaymentInProgressException, ExpiredBankAccountException {
        paymentService.accountPay(userId, account);
        return ResponseEntity.ok().build();
    }
}
