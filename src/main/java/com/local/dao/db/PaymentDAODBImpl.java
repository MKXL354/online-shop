package com.local.dao.db;

import com.local.dao.DAOException;
import com.local.dao.PaymentDAO;
import com.local.dao.transaction.TransactionManagerException;
import com.local.model.*;
import com.local.dao.transaction.TransactionManager;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;

public class PaymentDAODBImpl implements PaymentDAO {
    private TransactionManager transactionManager;

    public PaymentDAODBImpl(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Payment addPayment(Payment payment) throws DAOException {
        String query = "insert into PAYMENTS(ID, USER_ID, CART_ID, AMOUNT, LAST_UPDATE_TIME, PAYMENT_STATUS) values(?,?,?,?,?,?)";
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, payment.getId());
            statement.setInt(2, payment.getUser().getId());
            statement.setInt(3, payment.getCart().getId());
            statement.setBigDecimal(4, payment.getAmount());
            statement.setTimestamp(5, Timestamp.valueOf(payment.getLastUpdateTime()));
            statement.setString(6, payment.getPaymentStatus().toString());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                payment.setId(generatedKeys.getInt(1));
                return payment;
            }
        }
        catch(TransactionManagerException e){
            throw new DAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new DAOException("unexpected exception", e);
        }
        finally {
            transactionManager.closeConnection(connection);
        }
    }

    @Override
    public Payment getPendingPayment(int userId) throws DAOException {
        String query = "select ID, CART_ID, AMOUNT, LAST_UPDATE_TIME, PAYMENT_STATUS from PAYMENTS where USER_ID = ? and PAYMENT_STATUS = ?";
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, userId);
            statement.setString(2, PaymentStatus.PENDING.toString());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                int paymentId = resultSet.getInt("ID");
                int cartId = resultSet.getInt("CART_ID");
                BigDecimal amount = resultSet.getBigDecimal("AMOUNT");
                LocalDateTime lastUpdateTime = LocalDateTime.parse(resultSet.getString("LAST_UPDATE_TIME"));
                PaymentStatus paymentStatus = PaymentStatus.valueOf(resultSet.getString("PAYMENT_STATUS"));

                User user = new User(userId, null, null, null, null);
                Cart cart = new Cart(cartId, null, null, null, null);
                return new Payment(paymentId, user, cart, amount, lastUpdateTime, paymentStatus);
            }
            else{
                return null;
            }
        }
        catch(TransactionManagerException e){
            throw new DAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new DAOException("unexpected exception", e);
        }
        finally {
            transactionManager.closeConnection(connection);
        }
    }

    @Override
    public void updatePayment(Payment payment) throws DAOException {
        String query = "update PAYMENTS set AMOUNT = ?, LAST_UPDATE_TIME = ?, PAYMENT_STATUS = ? where ID = ?";
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setBigDecimal(1, payment.getAmount());
            statement.setTimestamp(2, Timestamp.valueOf(payment.getLastUpdateTime()));
            statement.setString(3, payment.getPaymentStatus().toString());
            statement.setInt(4, payment.getId());
            statement.executeUpdate();
        }
        catch(TransactionManagerException e){
            throw new DAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new DAOException("unexpected exception", e);
        }
        finally {
            transactionManager.closeConnection(connection);
        }
    }
}