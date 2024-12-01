package com.local.dao.cart;

import com.local.dao.DAOException;
import com.local.dao.dbconnector.DataBaseConnectionException;
import com.local.model.*;
import com.local.util.transaction.DBTransactionManager;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class CartDAODBImpl implements CartDAO{
    private DBTransactionManager transactionManager;

    public CartDAODBImpl(DBTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Cart getActiveCart(User user) throws DAOException {
        String query = "select ID, LAST_UPDATE_TIME from CARTS where USER_ID = ? and CART_STATUS = ?";
        try(Connection conn = transactionManager.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)){

            statement.setInt(1, user.getId());
            statement.setString(2, CartStatus.ACTIVE.toString());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                int id = resultSet.getInt("ID");
                HashMap<Integer, Product> products = getProductsInCart(id);
                LocalDateTime lastUpdateTime = LocalDateTime.parse(resultSet.getString("LAST_UPDATE_TIME"));
                return new Cart(id, user, products, lastUpdateTime, CartStatus.ACTIVE);
            }
            else{
                return null;
            }
        }
        catch(DataBaseConnectionException e){
            throw new DAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new DAOException("unexpected exception", e);
        }
    }
    
    @Override
    public HashSet<Cart> getAllActiveCarts() throws DAOException {
        HashSet<Cart> activeCarts = new HashSet<>();
        String query = "select CARTS.ID as CART_ID, USER_ID, LAST_UPDATE_TIME, CART_STATUS, USERNAME, PASSWORD, USER_TYPE, BALANCE " +
                "from CARTS inner join USERS on CARTS.USER_ID = USERS.ID " +
                "where CART_STATUS = ?";
        try(Connection conn = transactionManager.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)){

            statement.setString(1, CartStatus.ACTIVE.toString());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                int cartId = resultSet.getInt("CART_ID");
                HashMap<Integer, Product> products = getProductsInCart(cartId);
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
            return activeCarts;
        }
        catch(DataBaseConnectionException e){
            throw new DAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new DAOException("unexpected exception", e);
        }
    }

    @Override
    public Cart addCartToUser(User user) throws DAOException {
        String query = "insert into CARTS(USER_ID, LAST_UPDATE_TIME, CART_STATUS) values (?, ?, ?)";
        try(Connection conn = transactionManager.getConnection();
            PreparedStatement statement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)){

            LocalDateTime now = LocalDateTime.now();
            statement.setInt(1, user.getId());
            statement.setTimestamp(2, Timestamp.valueOf(now));
            statement.setString(3, CartStatus.ACTIVE.toString());
            statement.executeUpdate();
            try(ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                int id = generatedKeys.getInt("ID");
                return new Cart(id, user, new ConcurrentHashMap<Integer, Product>(), now, CartStatus.ACTIVE);
            }
        }
        catch(DataBaseConnectionException e){
            throw new DAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new DAOException("unexpected exception", e);
        }
    }
    
    @Override
    public void updateCart(Cart cart) throws DAOException {
        
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
        try(Connection conn = transactionManager.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)){

            statement.setInt(1, cart.getId());
            statement.setInt(2, product.getId());
            statement.executeUpdate();
        }
        catch(DataBaseConnectionException e){
            throw new DAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new DAOException("unexpected exception", e);
        }
    }
    
    private HashMap<Integer, Product> getProductsInCart(int cartId) throws DAOException {
        HashMap<Integer, Product> products = new HashMap<>();
        String query = "select ID, NAME, PRICE, PRODUCT_TYPE, PRODUCT_STATUS " +
                "from CARTS_PRODUCTS inner join PRODUCTS on PRODUCT_ID = ID " +
                "where CART_ID = ?";
        try(Connection conn = transactionManager.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)){

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
        catch(DataBaseConnectionException e){
            throw new DAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new DAOException("unexpected exception", e);
        }
    }
}
