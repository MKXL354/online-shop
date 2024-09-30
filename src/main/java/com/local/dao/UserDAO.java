package com.local.dao;

import com.local.model.User;
import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {
    private JdbcConnectionPool connectionPool;

    public UserDAO(JdbcConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void insertUser(User user) {
        try(Connection conn = connectionPool.getConnection()){
            String query = "insert into USERS(ID, USERNAME, PASSWORD) values(?,?,?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setObject(1, user.getId());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
        }
        catch(SQLException e){
//            TODO: exception handling: reconnection and constraint breakage logic
            e.printStackTrace();
        }
    }
}
