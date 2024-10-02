package com.local.dao;

import com.local.db.ConnectionPool;
import com.local.db.DataBaseConnectionException;
import com.local.model.User;
import com.local.service.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private ConnectionPool connectionPool;

    public UserDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void insertUser(User user) throws DataAccessException {
//        TODO: also do validation in the upper layer (like request object validation)
        try(Connection conn = connectionPool.getConnection()){
            String query = "insert into USERS(USERNAME, PASSWORD) values(?,?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
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
            statement.setObject(3, user.getUserType());
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        }
        catch(DataBaseConnectionException e){
            throw new DataAccessException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new DataAccessException("constraint violation or user does not exist", e);
        }
    }

    public void deleteUser(int id) throws DataAccessException {
        try(Connection conn = connectionPool.getConnection()){
            String query = "delete from USERS where ID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        }
        catch(DataBaseConnectionException e){
            throw new DataAccessException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new DataAccessException("user does not exist", e);
        }
    }

    public User findUserById(int id) throws DataAccessException {
        User user = null;
        try(Connection conn = connectionPool.getConnection()){
            String query = "select * from USERS where ID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                String username = resultSet.getString("USERNAME");
                String password = resultSet.getString("PASSWORD");
                UserType type = (UserType)resultSet.getObject("TYPE");
                user = new User(id, username, password, type);
            }
        }
        catch(DataBaseConnectionException e){
            throw new DataAccessException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new DataAccessException("user does not exist", e);
        }
        return user;
    }
}
