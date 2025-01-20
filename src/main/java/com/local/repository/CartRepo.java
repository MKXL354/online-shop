package com.local.repository;

import com.local.entity.Cart;
import com.local.entity.CartStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Cart> findByAppUserIdAndCartStatus(Long userId, CartStatus cartStatus);
}
