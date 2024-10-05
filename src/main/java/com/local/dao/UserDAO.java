package com.local.dao;

import com.local.db.ConnectionPool;
import com.local.db.DataBaseConnectionException;
import com.local.model.User;
import com.local.model.UserType;

import java.sql.*;

public class UserDAO {
    private ConnectionPool connectionPool;

    public UserDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void addUser(User user) throws DataAccessException {
        try(Connection conn = connectionPool.getConnection()){
            String query = "insert into USERS(USERNAME, PASSWORD, TYPE) values(?,?,?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getType().toString());
            statement.executeUpdate();
        }
        catch(DataBaseConnectionException e){
            throw new DataAccessException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new DataAccessException("constraint violation", e);
        }
    }

    public void updateUser(User user) throws DataAccessException {
        try(Connection conn = connectionPool.getConnection()){
            String query = "update USERS set USERNAME = ?, PASSWORD = ?, TYPE = ? where ID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getType().toString());
            statement.setInt(4, user.getId());
            int result = statement.executeUpdate();
            if(result == 0){
                throw new DataAccessException("user does not exist", null);
            }
        }
        catch(DataBaseConnectionException e){
            throw new DataAccessException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new DataAccessException("constraint violation", e);
        }
    }

    public void deleteUser(int id) throws DataAccessException {
        try(Connection conn = connectionPool.getConnection()){
            String query = "delete from USERS where ID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            int result = statement.executeUpdate();
            if(result == 0){
                throw new DataAccessException("user does not exist", null);
            }
        }
        catch(DataBaseConnectionException e){
            throw new DataAccessException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new DataAccessException("unexpected exception", e);
        }
    }

    public User findUserById(int id) throws DataAccessException {
        try(Connection conn = connectionPool.getConnection()){
            String query = "select * from USERS where ID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return createUserFromResultSet(resultSet);
            }
            else{
                return null;
            }
        }
        catch(DataBaseConnectionException e){
            throw new DataAccessException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new DataAccessException("unexpected exception", e);
        }
    }

    public User findUserByUsername(String username) throws DataAccessException {
        try(Connection conn = connectionPool.getConnection()){
            String query = "select * from USERS where USERNAME = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return createUserFromResultSet(resultSet);
            }
            else{
                return null;
            }
        }
        catch(DataBaseConnectionException e){
            throw new DataAccessException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new DataAccessException("unexpected exception", e);
        }
    }

    private User createUserFromResultSet(ResultSet resultSet) throws DataAccessException {
        try{
            int id = resultSet.getInt("ID");
            String username = resultSet.getString("USERNAME");
            String password = resultSet.getString("PASSWORD");
            UserType type = UserType.valueOf(resultSet.getString("TYPE"));
            return new User(id, username, password, type);
        }
        catch(SQLException e){
            throw new DataAccessException("unexpected exception", e);
        }
    }
}
//TODO: check service constraints programmatically
