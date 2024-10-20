package com.local.dao.cart;

import com.local.dao.DAOException;
import com.local.db.ConnectionPool;
import com.local.db.DataBaseConnectionException;
import com.local.model.Cart;
import com.local.model.Product;
import com.local.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class CartDAODBImpl implements CartDAO{
    private ConnectionPool connectionPool;

    public CartDAODBImpl(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Product getProductInCartById(int cartId, int productId) throws DAOException {
        String query = "select ID, NAME, PRICE, CARTS_PRODUCTS.COUNT " +
                "from PRODUCTS inner join CARTS_PRODUCTS on ID = PRODUCT_ID " +
                "where CART_ID = ? and PRODUCT_ID = ? ";
        try(Connection conn = connectionPool.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)){

            statement.setInt(1, cartId);
            statement.setInt(2, productId);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    String name = resultSet.getString("NAME");
                    float price = resultSet.getFloat("PRICE");
                    int count = resultSet.getInt("COUNT");
                    return new Product(productId, name, price, count);
                }
                else{
                    return null;
                }
            }
        }
        catch(DataBaseConnectionException e){
            throw new DAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            throw new DAOException("unexpected exception", e);
        }
    }

    @Override
    public void addProductToCart(Cart cart, Product product) throws DAOException {
        String query = "insert into CARTS_PRODUCTS(CART_ID, PRODUCT_ID, COUNT) values (?, ?, ?)";
        try(Connection conn = connectionPool.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)){
            statement.setInt(1, cart.getId());
            statement.setInt(2, product.getId());
            statement.setInt(3, product.getCount());
            statement.executeUpdate();
        }
        catch(DataBaseConnectionException e){
            throw new DAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            throw new DAOException("unexpected exception", e);
        }
    }

    @Override
    public void updateProductInCart(Cart cart, Product product) throws DAOException {
        String query = "update CARTS_PRODUCTS set COUNT = ? where CART_ID = ? and Product_ID = ?";
        try(Connection conn = connectionPool.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)){

            statement.setInt(1, product.getCount());
            statement.setInt(2, cart.getId());
            statement.setInt(3, product.getId());
            statement.executeUpdate();
        }
        catch(DataBaseConnectionException e){
            throw new DAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            throw new DAOException("unexpected exception", e);
        }
    }

    @Override
    public Cart getActiveCart(User user) throws DAOException {
        String query = "select ID from CARTS where USER_ID = ? and PROCESS_TIME is null";
        try(Connection conn = connectionPool.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)){

            statement.setInt(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                int id = resultSet.getInt("ID");
                Set<Product> products = getProductsInCart(id);
                return new Cart(id, user, products, null);
            }
            else{
                return null;
            }
        }
        catch(DataBaseConnectionException e){
            throw new DAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            throw new DAOException("unexpected exception", e);
        }
    }

    @Override
    public Cart addCartToUser(User user) throws DAOException {
        String query = "insert into CARTS(USER_ID, PROCESS_TIME) values (?, ?)";
        try(Connection conn = connectionPool.getConnection();
            PreparedStatement statement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)){

            statement.setInt(1, user.getId());
            statement.setDate(2, null);
            statement.executeUpdate();
            try(ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                int id = generatedKeys.getInt("ID");
                Set<Product> products = getProductsInCart(id);
                return new Cart(id, user, products, null);
            }
        }
        catch(DataBaseConnectionException e){
            throw new DAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            throw new DAOException("unexpected exception", e);
        }
    }

    @Override
    public Set<Product> getProductsInCart(int cartId) throws DAOException {
        Set<Product> products = new HashSet<>();
        String query = "select ID, NAME, PRICE, PRODUCTS.COUNT " +
                "from CARTS_PRODUCTS inner join PRODUCTS on PRODUCT_ID = ID " +
                "where CART_ID = ?";
        try(Connection conn = connectionPool.getConnection();
            PreparedStatement statement = conn.prepareStatement(query)){

            statement.setInt(1, cartId);
            try(ResultSet resultSet = statement.executeQuery()){
                while(resultSet.next()){
                    int id = resultSet.getInt("ID");
                    String name = resultSet.getString("NAME");
                    float price = resultSet.getFloat("PRICE");
                    int count = resultSet.getInt("COUNT");
                    products.add(new Product(id, name, price, count));
                }
                return products;
            }
        }
        catch(DataBaseConnectionException e){
            throw new DAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
            throw new DAOException("unexpected exception", e);
        }
    }
}
