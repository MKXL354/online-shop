package com.local.dao.db;

import com.local.dao.CartDAO;
import com.local.dao.DAOException;
import com.local.model.*;
import com.local.dao.transaction.TransactionManager;
import com.local.dao.transaction.TransactionManagerException;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class CartDAODBImpl implements CartDAO {
    private TransactionManager transactionManager;

    public CartDAODBImpl(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Cart getActiveCart(User user) throws DAOException {
        String query = "select ID, LAST_UPDATE_TIME from CARTS where USER_ID = ? and CART_STATUS = ?";
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, user.getId());
            statement.setString(2, CartStatus.ACTIVE.toString());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                int id = resultSet.getInt("ID");
                HashMap<Integer, Product> products = getProductsInCart(id);
                LocalDateTime lastUpdateTime = LocalDateTime.parse(resultSet.getString("LAST_UPDATE_TIME"));
                transactionManager.closeConnection(connection);
                return new Cart(id, user, products, lastUpdateTime, CartStatus.ACTIVE);
            }
            else{
                transactionManager.closeConnection(connection);
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
    @Override
    public HashSet<Cart> getAllActiveCarts() throws DAOException {
        HashSet<Cart> activeCarts = new HashSet<>();
        String query = "select CARTS.ID as CART_ID, USER_ID, LAST_UPDATE_TIME, CART_STATUS, USERNAME, PASSWORD, USER_TYPE, BALANCE " +
                "from CARTS inner join USERS on CARTS.USER_ID = USERS.ID " +
                "where CART_STATUS = ?";
        Connection connection = null;

        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, CartStatus.ACTIVE.toString());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                int cartId = resultSet.getInt("CART_ID");
                LocalDateTime lastUpdateTime = LocalDateTime.parse(resultSet.getString("LAST_UPDATE_TIME"));
                CartStatus cartStatus = CartStatus.valueOf(resultSet.getString("CART_STATUS"));

                int userId = resultSet.getInt("USER_ID");
                String username = resultSet.getString("USERNAME");
                String password = resultSet.getString("PASSWORD");
                UserType userType = UserType.valueOf(resultSet.getString("USER_TYPE"));
                BigDecimal balance = resultSet.getBigDecimal("BALANCE");

                User user = new User(userId, username, password, userType, balance);
                activeCarts.add(new Cart(cartId, user, getProductsInCart(cartId), lastUpdateTime, cartStatus));
            }
            transactionManager.closeConnection(connection);
            return activeCarts;
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
    public Cart addCartToUser(User user) throws DAOException {
        String query = "insert into CARTS(USER_ID, LAST_UPDATE_TIME, CART_STATUS) values (?, ?, ?)";
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            LocalDateTime now = LocalDateTime.now();
            statement.setInt(1, user.getId());
            statement.setTimestamp(2, Timestamp.valueOf(now));
            statement.setString(3, CartStatus.ACTIVE.toString());
            statement.executeUpdate();
            try(ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                int id = generatedKeys.getInt("ID");
                return new Cart(id, user, new ConcurrentHashMap<>(), now, CartStatus.ACTIVE);
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
    
    private HashMap<Integer, Product> getProductsInCart(int cartId) throws DAOException {
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
