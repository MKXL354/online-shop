package com.local.service;

import com.local.entity.Payment;
import com.local.entity.PaymentStatus;
import com.local.exception.service.payment.*;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.persistence.CardDAO;
import com.local.persistence.DAOException;
import com.local.persistence.PaymentDAO;
import com.local.persistence.transaction.ManagedTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PaymentService {
    private CommonService commonService;
    private PaymentDAO paymentDAO;
    private CardDAO cardDAO;
    private ConcurrentHashMap<Integer, Payment> inProgressPayments = new ConcurrentHashMap<>();

    @Autowired
    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    @Autowired
    public void setPaymentDAO(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    @Autowired
    public void setCardDAO(CardDAO cardDAO) {
        this.cardDAO = cardDAO;
    }

    @ManagedTransaction
    public Card addCard(Card card) throws DuplicateCardException, DAOException {
        if (cardDAO.getCardById(card.getId()) != null) {
            throw new DuplicateCardException("card already exists", null);
        }
        return cardDAO.addCard(card);
    }

    @ManagedTransaction
    public void addBalance(int cardId, BigDecimal amount) throws CardNotFoundException, DAOException {
        Card card;
        if ((card = cardDAO.getCardById(cardId)) == null) {
            throw new CardNotFoundException("card not found", null);
        }
        card.setBalance(card.getBalance().add(amount));
        cardDAO.updateCard(card);
    }

    @ManagedTransaction
    public void cardPay(int userId, Card card) throws UserNotFoundException, PendingPaymentNotFoundException, InvalidCardException, InsufficientBalanceException, PaymentInProgressException, DAOException {
        Payment payment = commonService.getPendingPayment(userId);
        if (payment == null) {
            throw new PendingPaymentNotFoundException("no active payment found", null);
        }
        Card actualCard = cardDAO.getCardById(card.getId());
        if (actualCard == null || !actualCard.getNumber().equals(card.getNumber()) || !actualCard.getPassword().equals(card.getPassword()) || !actualCard.getExpiryDate().equals(card.getExpiryDate())) {
            throw new InvalidCardException("invalid card details", null);
        }
        if (actualCard.getBalance().compareTo(payment.getAmount()) < 0) {
            throw new InsufficientBalanceException("insufficient account balance", null);
        }
        if (inProgressPayments.putIfAbsent(payment.getId(), payment) != null) {
            throw new PaymentInProgressException("payment already in progress", null);
        }

        try {
            actualCard.setBalance(actualCard.getBalance().subtract(payment.getAmount()));
            cardDAO.updateCard(actualCard);

            payment.setLastUpdateTime(LocalDateTime.now());
            payment.setPaymentStatus(PaymentStatus.SUCCESSFUL);
            paymentDAO.updatePayment(payment);
        } finally {
            inProgressPayments.remove(payment.getId());
        }
    }
}
//TODO: payment cache upper in the layers?
