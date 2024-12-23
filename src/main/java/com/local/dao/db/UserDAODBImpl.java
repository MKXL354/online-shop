package com.local.dao.db;

import com.local.dao.DAOException;
import com.local.dao.UserDAO;
import com.local.dao.transaction.TransactionManager;
import com.local.dao.transaction.TransactionManagerException;
import com.local.model.User;
import com.local.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;

@Repository
public class UserDAODBImpl implements UserDAO {
    private TransactionManager transactionManager;

    @Autowired
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public User addUser(User user) throws DAOException {
        String query = "insert into USERS(USERNAME, PASSWORD, USER_TYPE, BALANCE) values(?, ?, ?, ?)";
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getType().toString());
            statement.setBigDecimal(4, user.getBalance());
            statement.executeUpdate();
            try(ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                int id = generatedKeys.getInt("ID");
                user.setId(id);
                return user;
            }
        }
        catch(TransactionManagerException e){
            throw new DAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new DAOException("constraint violation", e);
        }
        finally {
            transactionManager.closeConnection(connection);
        }
    }

    @Override
    public void updateUser(User user) throws DAOException {
        String query = "update USERS set PASSWORD = ?, USER_TYPE = ?, BALANCE = ? where ID = ?";
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, user.getPassword());
            statement.setString(2, user.getType().toString());
            statement.setBigDecimal(3, user.getBalance());
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        }
        catch(TransactionManagerException e){
            throw new DAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new DAOException("constraint violation", e);
        }
        finally {
            transactionManager.closeConnection(connection);
        }
    }

    @Override
    public User getUserById(int id) throws DAOException {
        return getUserByIdentifier("ID", id, JDBCType.INTEGER);
    }

    @Override
    public User getUserByUsername(String username) throws DAOException {
        return getUserByIdentifier("USERNAME", username, JDBCType.VARCHAR);
    }

    private User getUserByIdentifier(String columnName, Object value, SQLType type) throws DAOException {
        String query = String.format("select * from USERS where %s = ?", columnName);
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setObject(1, value, type);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return createUserFromResultSet(resultSet);
                }
                else{
                    return null;
                }
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

    private User createUserFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("ID");
        String username = resultSet.getString("USERNAME");
        String password = resultSet.getString("PASSWORD");
        UserType userType = UserType.valueOf(resultSet.getString("USER_TYPE"));
        BigDecimal balance = resultSet.getBigDecimal("BALANCE");
        return new User(id, username, password, userType, balance);
    }
}