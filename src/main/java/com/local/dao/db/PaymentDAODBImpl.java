package com.local.dao.db;

import com.local.dao.DAOException;
import com.local.dao.PaymentDAO;
import com.local.dao.transaction.TransactionManagerException;
import com.local.model.*;
import com.local.dao.transaction.TransactionManager;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;

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
    public Payment getPendingPayment(User user) throws DAOException {
        String query = "select p.ID as P_ID, AMOUNT, p.LAST_UPDATE_TIME as P_LAST_UPDATE_TIME, PAYMENT_STATUS, CART_ID, c.LAST_UPDATE_TIME as C_LAST_UPDATE_TIME, CART_STATUS " +
                "from PAYMENTS p inner join CARTS c on p.CART_ID = c.ID " +
                "where p.USER_ID = ? and PAYMENT_STATUS = ?";
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, user.getId());
            statement.setString(2, PaymentStatus.PENDING.toString());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                int paymentId = resultSet.getInt("P_ID");
                BigDecimal amount = resultSet.getBigDecimal("AMOUNT");
                LocalDateTime lastUpdateTime = LocalDateTime.parse(resultSet.getString("P_LAST_UPDATE_TIME"));
                PaymentStatus paymentStatus = PaymentStatus.valueOf(resultSet.getString("PAYMENT_STATUS"));

                int cartId = resultSet.getInt("CART_ID");
                LocalDateTime cartLastUpdateTime = LocalDateTime.parse(resultSet.getString("C_LAST_UPDATE_TIME"));
                CartStatus cartStatus = CartStatus.valueOf(resultSet.getString("CART_STATUS"));

                return new Payment(paymentId, user, new Cart(cartId, user, null, cartLastUpdateTime, cartStatus), amount, lastUpdateTime, paymentStatus);
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
    public HashSet<Payment> getAllPendingPayments() throws DAOException {
        return null;
    }

    @Override
    public HashSet<Payment> getAllCancelledPayments() throws DAOException {
        return null;
    }

    @Override
    public HashSet<Payment> getAllPayments() throws DAOException {
        return null;
    }

    @Override
    public void updatePayment(Payment payment) throws DAOException {

    }
}
//TODO: write later using general queries