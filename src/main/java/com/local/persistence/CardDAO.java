package com.local.persistence;

import com.local.model.Card;

public interface CardDAO {
    Card addCard(Card card) throws DAOException;
    Card getCardById(int id) throws DAOException;
    void updateCard(Card card) throws DAOException;
}
