package com.local.persistence.db;

import com.local.persistence.CartDAO;
import com.local.persistence.DAOException;
import com.local.model.*;
import com.local.persistence.transaction.TransactionManager;
import com.local.persistence.transaction.TransactionManagerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;

@Repository
public class CartDAODBImpl implements CartDAO {
    private TransactionManager transactionManager;

    @Autowired
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Cart getCartById(int cartId) throws DAOException{
        String query = "select USER_ID, LAST_UPDATE_TIME, CART_STATUS from CARTS where ID = ?";
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, cartId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                LocalDateTime lastUpdateTime = resultSet.getTimestamp("LAST_UPDATE_TIME").toLocalDateTime();
                CartStatus cartStatus = CartStatus.valueOf(resultSet.getString("CART_STATUS"));

                int userId = resultSet.getInt("USER_ID");
                User user = new User(userId, null, null, null);
                return new Cart(cartId, user, null, lastUpdateTime, cartStatus);
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
    public Cart getActiveCart(int userId) throws DAOException {
        String query = "select ID, LAST_UPDATE_TIME from CARTS where USER_ID = ? and CART_STATUS = ?";
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, userId);
            statement.setString(2, CartStatus.ACTIVE.toString());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                int id = resultSet.getInt("ID");
                LocalDateTime lastUpdateTime = resultSet.getTimestamp("LAST_UPDATE_TIME").toLocalDateTime();

                User user = new User(userId, null, null, null);
                return new Cart(id, user, null, lastUpdateTime, CartStatus.ACTIVE);
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

    /*
    here because of the schedulers. if those are removed this too will be removed
     */
//    @Override
//    public HashSet<Cart> getAllActiveCarts() throws DAOException {
//        HashSet<Cart> activeCarts = new HashSet<>();
//        String query = "select CARTS.ID as CART_ID, USER_ID, LAST_UPDATE_TIME, CART_STATUS, USERNAME, PASSWORD, USER_TYPE, BALANCE " +
//                "from CARTS inner join USERS on CARTS.USER_ID = USERS.ID " +
//                "where CART_STATUS = ?";
//        Connection connection = null;
//
//        try{
//            connection = transactionManager.openConnection();
//            PreparedStatement statement = connection.prepareStatement(query);
//
//            statement.setString(1, CartStatus.ACTIVE.toString());
//            ResultSet resultSet = statement.executeQuery();
//            while(resultSet.next()){
//                int cartId = resultSet.getInt("CART_ID");
//                LocalDateTime lastUpdateTime = resultSet.getTimestamp("LAST_UPDATE_TIME").toLocalDateTime();
//                CartStatus cartStatus = CartStatus.valueOf(resultSet.getString("CART_STATUS"));
//
//                int userId = resultSet.getInt("USER_ID");
//                String username = resultSet.getString("USERNAME");
//                String password = resultSet.getString("PASSWORD");
//                UserType userType = UserType.valueOf(resultSet.getString("USER_TYPE"));
//                BigDecimal balance = resultSet.getBigDecimal("BALANCE");
//
//                User user = new User(userId, username, password, userType, balance);
//                activeCarts.add(new Cart(cartId, user, getProductsInCart(cartId), lastUpdateTime, cartStatus));
//            }
//            return activeCarts;
//        }
//        catch(TransactionManagerException e){
//            throw new DAOException(e.getMessage(), e);
//        }
//        catch(SQLException e){
//            throw new DAOException("unexpected exception", e);
//        }
//        finally {
//            transactionManager.closeConnection(connection);
//        }
//    }

    @Override
    public Cart addCartToUser(int userId) throws DAOException {
        String query = "insert into CARTS(USER_ID, LAST_UPDATE_TIME, CART_STATUS) values (?, ?, ?)";
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            LocalDateTime now = LocalDateTime.now();
            statement.setInt(1, userId);
            statement.setTimestamp(2, Timestamp.valueOf(now));
            statement.setString(3, CartStatus.ACTIVE.toString());
            statement.executeUpdate();
            try(ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                int id = generatedKeys.getInt("ID");
                User user = new User(userId, null, null, null);
                return new Cart(id, user, null, now, CartStatus.ACTIVE);
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
    public void updateCart(Cart cart) throws DAOException {
        String query = "update CARTS set LAST_UPDATE_TIME = ?, CART_STATUS = ? where ID = ?";
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setTimestamp(1, Timestamp.valueOf(cart.getLastUpdateTime()));
            statement.setString(2, cart.getCartStatus().toString());
            statement.setInt(3, cart.getId());
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

    @Override
    public void addProductToCart(Cart cart, Product product) throws DAOException {
        String query = "insert into CARTS_PRODUCTS(CART_ID, PRODUCT_ID) values (?, ?)";
        updateProductInCart(query, cart, product);
    }

    @Override
    public void removeProductFromCart(Cart cart, Product product) throws DAOException {
        String query = "delete from CARTS_PRODUCTS where CART_ID = ? and Product_ID = ?";
        updateProductInCart(query, cart, product);
    }

    private void updateProductInCart(String query, Cart cart, Product product) throws DAOException {
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, cart.getId());
            statement.setInt(2, product.getId());
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

    @Override
    public HashMap<Integer, Product> getProductsInCart(int cartId) throws DAOException {
        HashMap<Integer, Product> products = new HashMap<>();
        String query = "select ID, NAME, PRICE, PRODUCT_TYPE, PRODUCT_STATUS " +
                "from CARTS_PRODUCTS inner join PRODUCTS on PRODUCT_ID = ID " +
                "where CART_ID = ?";
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, cartId);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("NAME");
                    BigDecimal price = resultSet.getBigDecimal("PRICE");
                    ProductType productType = ProductType.valueOf(resultSet.getString("PRODUCT_TYPE"));
                    ProductStatus productStatus = ProductStatus.valueOf(resultSet.getString("PRODUCT_STATUS"));
                    products.put(id, new Product(id, name, price, productType, productStatus));
                }
                return products;
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
}
