package com.local.persistence;

public interface CardDAO {
    Card addCard(Card card) throws DAOException;

    Card getCardById(int id) throws DAOException;

    void updateCard(Card card) throws DAOException;
}
