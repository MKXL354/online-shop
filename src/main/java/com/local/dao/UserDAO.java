package com.local.dao;

import com.local.commonexceptions.ServiceException;
import com.local.db.ConnectionPool;
import com.local.db.DataBaseConnectionException;
import com.local.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {
    private ConnectionPool connectionPool;

    public UserDAO(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void insertUser(User user) throws ServiceException {
        try(Connection conn = connectionPool.getConnection()){
            String query = "insert into USERS(USERNAME, PASSWORD) values(?,?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
        }
        catch(DataBaseConnectionException e){
            throw new ServiceException(e.getMessage(), e);
        }
        catch(SQLException e){
//            TODO: remove this
//            e.getCause().printStackTrace();
            throw new ServiceException("constraint violation", e);
        }
    }
}
