package com.local.persistence.db;

import com.local.persistence.DAOException;
import com.local.persistence.PaymentDAO;
import com.local.persistence.transaction.TransactionManagerException;
import com.local.model.*;
import com.local.persistence.transaction.TransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;

@Repository
public class PaymentDAODBImpl implements PaymentDAO {
    private TransactionManager transactionManager;

    @Autowired
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Payment addPayment(Payment payment) throws DAOException {
        String query = "insert into PAYMENTS(ID, USER_ID, CART_ID, AMOUNT, LAST_UPDATE_TIME, PAYMENT_STATUS) values(?,?,?,?,?,?)";
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, payment.getId());
            statement.setInt(2, payment.getUser().getId());
            statement.setInt(3, payment.getCart().getId());
            statement.setBigDecimal(4, payment.getAmount());
            statement.setTimestamp(5, Timestamp.valueOf(payment.getLastUpdateTime()));
            statement.setString(6, payment.getPaymentStatus().toString());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                payment.setId(generatedKeys.getInt("ID"));
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
                LocalDateTime lastUpdateTime = resultSet.getTimestamp("LAST_UPDATE_TIME").toLocalDateTime();
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