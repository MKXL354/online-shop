package com.local.dao;

import com.local.db.ConnectionPool;
import com.local.db.DataBaseConnectionException;
import com.local.model.User;
import com.local.model.UserType;

import java.sql.*;

public class UserDAOImpl implements UserDAO{
    private ConnectionPool connectionPool;

    public UserDAOImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void addUser(User user) throws UserDAOException {
        String query = "insert into USERS(USERNAME, PASSWORD, TYPE) values(?,?,?)";
        try(Connection conn = connectionPool.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)){

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getType().toString());
            statement.executeUpdate();
        }
        catch(DataBaseConnectionException e){
            throw new UserDAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new UserDAOException("constraint violation", e);
        }
    }

    @Override
    public void updateUser(User user) throws UserDAOException {
        String query = "update USERS set USERNAME = ?, PASSWORD = ?, TYPE = ? where ID = ?";
        try(Connection conn = connectionPool.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)){

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getType().toString());
            statement.setInt(4, user.getId());
            int result = statement.executeUpdate();
            if(result == 0){
                throw new UserDAOException("user does not exist", null);
            }
        }
        catch(DataBaseConnectionException e){
            throw new UserDAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new UserDAOException("constraint violation", e);
        }
    }

    @Override
    public void deleteUser(int id) throws UserDAOException {
        String query = "delete from USERS where ID = ?";
        try(Connection conn = connectionPool.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)){

            statement.setInt(1, id);
            int result = statement.executeUpdate();
            if(result == 0){
                throw new UserDAOException("user does not exist", null);
            }
        }
        catch(DataBaseConnectionException e){
            throw new UserDAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new UserDAOException("unexpected exception", e);
        }
    }

    @Override
    public User findUserById(int id) throws UserDAOException {
        String query = "select * from USERS where ID = ?";
        try(Connection conn = connectionPool.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)){

            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return createUserFromResultSet(resultSet);
                }
                else{
                    return null;
                }
            }
        }
        catch(DataBaseConnectionException e){
            throw new UserDAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new UserDAOException("unexpected exception", e);
        }
    }

    @Override
    public User findUserByUsername(String username) throws UserDAOException {
        String query = "select * from USERS where USERNAME = ?";
        try(Connection conn = connectionPool.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)){

            statement.setString(1, username);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return createUserFromResultSet(resultSet);
                }
                else{
                    return null;
                }
            }
        }
        catch(DataBaseConnectionException e){
            throw new UserDAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new UserDAOException("unexpected exception", e);
        }
    }

    private User createUserFromResultSet(ResultSet resultSet) throws UserDAOException {
        try{
            int id = resultSet.getInt("ID");
            String username = resultSet.getString("USERNAME");
            String password = resultSet.getString("PASSWORD");
            UserType type = UserType.valueOf(resultSet.getString("TYPE"));
            return new User(id, username, password, type);
        }
        catch(SQLException e){
            throw new UserDAOException("unexpected exception", e);
        }
    }
}
