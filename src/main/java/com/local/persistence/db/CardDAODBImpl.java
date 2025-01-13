package com.local.persistence.db;

import com.local.persistence.CardDAO;
import com.local.persistence.DAOException;
import com.local.persistence.transaction.TransactionManager;
import com.local.persistence.transaction.TransactionManagerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

@Repository
public class CardDAODBImpl implements CardDAO {
    private TransactionManager transactionManager;

    @Autowired
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Card addCard(Card card) throws DAOException {
        String query = "insert into CARDS(NUMBER, PASSWORD, EXPIRY_DATE, BALANCE) values(?,?,?,?)";
        Connection connection = null;
        try {
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, card.getNumber());
            statement.setString(2, card.getPassword());
            statement.setDate(3, Date.valueOf(card.getExpiryDate()));
            statement.setBigDecimal(4, new BigDecimal(0));
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                card.setId(generatedKeys.getInt("ID"));
                return card;
            }
        } catch (TransactionManagerException e) {
            throw new DAOException(e.getMessage(), e);
        } catch (SQLException e) {
            throw new DAOException("constraint violation", e);
        } finally {
            transactionManager.closeConnection(connection);
        }
    }

    @Override
    public Card getCardById(int id) throws DAOException {
        String query = "select * from CARDS where ID = ?";
        Connection connection = null;
        try {
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String number = resultSet.getString("NUMBER");
                String password = resultSet.getString("PASSWORD");
                LocalDate expiryDate = resultSet.getDate("EXPIRY_DATE").toLocalDate();
                BigDecimal balance = resultSet.getBigDecimal("BALANCE");
                return new Card(id, number, password, expiryDate, balance);
            } else {
                return null;
            }
        } catch (TransactionManagerException e) {
            throw new DAOException(e.getMessage(), e);
        } catch (SQLException e) {
            throw new DAOException("unexpected exception", e);
        } finally {
            transactionManager.closeConnection(connection);
        }
    }

    @Override
    public void updateCard(Card card) throws DAOException {
        String query = "update CARDS set NUMBER = ?, PASSWORD = ?, EXPIRY_DATE = ?, BALANCE = ? where ID = ?";
        Connection connection = null;
        try {
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, card.getNumber());
            statement.setString(2, card.getPassword());
            statement.setDate(3, Date.valueOf(card.getExpiryDate()));
            statement.setBigDecimal(4, card.getBalance());
            statement.setInt(5, card.getId());
            statement.executeUpdate();
        } catch (TransactionManagerException e) {
            throw new DAOException(e.getMessage(), e);
        } catch (SQLException e) {
            throw new DAOException("unexpected exception", e);
        } finally {
            transactionManager.closeConnection(connection);
        }
    }
}
