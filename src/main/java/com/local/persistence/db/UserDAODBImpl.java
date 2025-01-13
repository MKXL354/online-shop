package com.local.persistence.db;

import com.local.entity.User;
import com.local.entity.UserType;
import com.local.persistence.DAOException;
import com.local.persistence.UserDAO;
import com.local.persistence.transaction.TransactionManager;
import com.local.persistence.transaction.TransactionManagerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
        String query = "insert into USERS(USERNAME, PASSWORD, USER_TYPE) values(?,?,?)";
        Connection connection = null;
        try {
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getType().toString());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                int id = generatedKeys.getInt("ID");
                user.setId(id);
                return user;
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
    public void updateUser(User user) throws DAOException {
        String query = "update USERS set PASSWORD = ?, USER_TYPE = ? where ID = ?";
        Connection connection = null;
        try {
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, user.getPassword());
            statement.setString(2, user.getType().toString());
            statement.setInt(3, user.getId());
            statement.executeUpdate();
        } catch (TransactionManagerException e) {
            throw new DAOException(e.getMessage(), e);
        } catch (SQLException e) {
            throw new DAOException("constraint violation", e);
        } finally {
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
        try {
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setObject(1, value, type);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createUserFromResultSet(resultSet);
                } else {
                    return null;
                }
            }
        } catch (TransactionManagerException e) {
            throw new DAOException(e.getMessage(), e);
        } catch (SQLException e) {
            throw new DAOException("unexpected exception", e);
        } finally {
            transactionManager.closeConnection(connection);
        }
    }

    private User createUserFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("ID");
        String username = resultSet.getString("USERNAME");
        String password = resultSet.getString("PASSWORD");
        UserType userType = UserType.valueOf(resultSet.getString("USER_TYPE"));
        return new User(id, username, password, userType);
    }
}