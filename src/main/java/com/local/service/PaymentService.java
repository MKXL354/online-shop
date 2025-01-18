package com.local.service;

import com.local.dto.BankAccountDto;
import com.local.entity.BankAccount;
import com.local.entity.Payment;
import com.local.entity.PaymentStatus;
import com.local.exception.service.payment.*;
import com.local.repository.BankAccountRepo;
import com.local.repository.PaymentRepo;
import com.local.util.password.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Transactional
public class PaymentService {
    private PasswordEncryptor passwordEncryptor;
    private PaymentRepo paymentRepo;
    private BankAccountRepo bankAccountRepo;
    private ConcurrentMap<Long, Payment> inProgressPayments = new ConcurrentHashMap<>();

    @Autowired
    public void setPasswordEncryptor(PasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }

    @Autowired
    public void setPaymentRepo(PaymentRepo paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    @Autowired
    public void setBankAccountRepo(BankAccountRepo bankAccountRepo) {
        this.bankAccountRepo = bankAccountRepo;
    }

    public Payment getPendingPayment(long userId) throws PendingPaymentNotFoundException {
        return paymentRepo.findPaymentByAppUserIdAndPaymentStatus(userId, PaymentStatus.PENDING).orElseThrow(() -> new PendingPaymentNotFoundException("no active payment found", null));
    }
    
    public BankAccountDto addBankAccount(BankAccountDto bankAccountDto) throws DuplicateBankAccountException {
        if(bankAccountRepo.findByNumber(bankAccountDto.getNumber()).isPresent()){
            throw new DuplicateBankAccountException("duplicate account number not allowed", null);
        }
        String hashedPassword = passwordEncryptor.hashPassword(bankAccountDto.getPassword());
        BankAccount bankAccount = bankAccountRepo.save(new BankAccount(bankAccountDto.getNumber(), hashedPassword, LocalDate.parse(bankAccountDto.getExpiryDate())));
        bankAccountDto.setId(bankAccount.getId());
        return bankAccountDto;
    }
    
    public void addBalance(long bankAccountId, BigDecimal amount) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepo.findById(bankAccountId).orElseThrow(() -> new BankAccountNotFoundException("bank account not found", null));
        bankAccount.setBalance(bankAccount.getBalance().add(amount));
        bankAccountRepo.save(bankAccount);
    }
    
    public void accountPay(long userId, BankAccountDto bankAccountDto) throws PendingPaymentNotFoundException, BankAccountNotFoundException, WrongBankAccountPasswordException, InsufficientBalanceException, ExpiredBankAccountException, PaymentInProgressException {
        Payment payment = getPendingPayment(userId);
        BankAccount actualBankAccount = bankAccountRepo.findByNumber(bankAccountDto.getNumber()).orElseThrow(() -> new BankAccountNotFoundException("invalid bank account details", null));
        if(!passwordEncryptor.checkPassword(bankAccountDto.getPassword(), actualBankAccount.getPassword())){
            throw new WrongBankAccountPasswordException("wrong account password", null);
        }
        if(actualBankAccount.getExpiryDate().isBefore(LocalDate.now())){
            throw new ExpiredBankAccountException("bank account is expired", null);
        }
        if (actualBankAccount.getBalance().compareTo(payment.getAmount()) < 0) {
            throw new InsufficientBalanceException("insufficient account balance", null);
        }
        if (inProgressPayments.putIfAbsent(payment.getId(), payment) != null) {
            throw new PaymentInProgressException("payment already in progress", null);
        }

        try {
            actualBankAccount.setBalance(actualBankAccount.getBalance().subtract(payment.getAmount()));
            bankAccountRepo.save(actualBankAccount);

            payment.setLastUpdateTime(LocalDateTime.now());
            payment.setPaymentStatus(PaymentStatus.SUCCESSFUL);
            paymentRepo.save(payment);
        } finally {
            inProgressPayments.remove(payment.getId());
        }
    }
}
