package com.local.repository;

import com.local.entity.Payment;
import com.local.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {
    Optional<Payment> findPaymentByAppUserIdAndPaymentStatus(long userId, PaymentStatus status);
}
