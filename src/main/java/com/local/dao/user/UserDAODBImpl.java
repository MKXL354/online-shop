//package com.local.dao.user;
//
//import com.local.dao.DAOException;
//import com.local.dbconnector.ConnectionPool;
//import com.local.dbconnector.DataBaseConnectionException;
//import com.local.model.User;
//import com.local.model.UserType;
//
//import java.sql.*;
//
//public class UserDAODBImpl implements UserDAO{
//    private ConnectionPool connectionPool;
//
//    public UserDAODBImpl(ConnectionPool connectionPool) {
//        this.connectionPool = connectionPool;
//    }
//
//    @Override
//    public void addUser(User user) throws DAOException {
//        String query = "insert into USERS(USERNAME, PASSWORD, TYPE) values(?,?,?)";
//        try(Connection conn = connectionPool.getConnection();
//            PreparedStatement statement = conn.prepareStatement(query)){
//
//            statement.setString(1, user.getUsername());
//            statement.setString(2, user.getPassword());
//            statement.setString(3, user.getType().toString());
//            statement.executeUpdate();
//        }
//        catch(DataBaseConnectionException e){
//            throw new DAOException(e.getMessage(), e);
//        }
//        catch(SQLException e){
//            throw new DAOException("constraint violation", e);
//        }
//    }
//
//    @Override
//    public User getUserById(int id) throws DAOException {
//        String query = "select * from USERS where ID = ?";
//        try(Connection conn = connectionPool.getConnection();
//            PreparedStatement statement = conn.prepareStatement(query)){
//
//            statement.setInt(1, id);
//            try(ResultSet resultSet = statement.executeQuery()){
//                if(resultSet.next()){
//                    return createUserFromResultSet(resultSet);
//                }
//                else{
//                    return null;
//                }
//            }
//        }
//        catch(DataBaseConnectionException e){
//            throw new DAOException(e.getMessage(), e);
//        }
//        catch(SQLException e){
//            throw new DAOException("unexpected exception", e);
//        }
//    }
//
//    @Override
//    public User getUserByUsername(String username) throws DAOException {
//        String query = "select * from USERS where USERNAME = ?";
//        try(Connection conn = connectionPool.getConnection();
//            PreparedStatement statement = conn.prepareStatement(query)){
//
//            statement.setString(1, username);
//            try(ResultSet resultSet = statement.executeQuery()){
//                if(resultSet.next()){
//                    return createUserFromResultSet(resultSet);
//                }
//                else{
//                    return null;
//                }
//            }
//        }
//        catch(DataBaseConnectionException e){
//            throw new DAOException(e.getMessage(), e);
//        }
//        catch(SQLException e){
//            throw new DAOException("unexpected exception", e);
//        }
//    }
//
//    private User createUserFromResultSet(ResultSet resultSet) throws DAOException {
//        try{
//            int id = resultSet.getInt("ID");
//            String username = resultSet.getString("USERNAME");
//            String password = resultSet.getString("PASSWORD");
//            UserType type = UserType.valueOf(resultSet.getString("TYPE"));
//            return new User(id, username, password, type);
//        }
//        catch(SQLException e){
//            throw new DAOException("unexpected exception", e);
//        }
//    }
//}
