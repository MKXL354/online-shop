//package com.local.dao.product;
//
//import com.local.dao.DAOException;
//import com.local.dbconnector.ConnectionPool;
//import com.local.dbconnector.DataBaseConnectionException;
//import com.local.model.Product;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ProductDAODBImpl implements ProductDAO {
//    private ConnectionPool connectionPool;
//
//    public ProductDAODBImpl(ConnectionPool ConnectionPool) {
//        this.connectionPool = ConnectionPool;
//    }
//
//    @Override
//    public void addProduct(Product product) throws DAOException {
//        String query = "insert into PRODUCTS(NAME, PRICE, COUNT) values(?,?,?)";
//        try(Connection conn = connectionPool.getConnection();
//            PreparedStatement statement = conn.prepareStatement(query)){
//
//            statement.setString(1, product.getName());
//            statement.setDouble(2, product.getPrice());
//            statement.setInt(3, product.getCount());
//            statement.executeUpdate();
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
//    public void updateProduct(Product product) throws DAOException {
//        String query = "update PRODUCTS set NAME = ?, PRICE = ?, COUNT = ? where ID = ?";
//        try(Connection conn = connectionPool.getConnection();
//            PreparedStatement statement = conn.prepareStatement(query)){
//
//            statement.setString(1, product.getName());
//            statement.setDouble(2, product.getPrice());
//            statement.setInt(3, product.getCount());
//            statement.setInt(4, product.getId());
//            statement.executeUpdate();
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
//    public Product getProductById(int id) throws DAOException {
//        String query = "select * from PRODUCTS where ID = ?";
//        try(Connection conn = connectionPool.getConnection();
//            PreparedStatement statement = conn.prepareStatement(query)){
//
//            statement.setInt(1, id);
//            try(ResultSet resultSet = statement.executeQuery()){
//                if(resultSet.next()){
//                    return createProductFromResultSet(resultSet);
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
//    public Product getProductByName(String name) throws DAOException {
//        String query = "select * from PRODUCTS where NAME = ?";
//        try(Connection conn = connectionPool.getConnection();
//            PreparedStatement statement = conn.prepareStatement(query)){
//
//            statement.setString(1, name);
//            try(ResultSet resultSet = statement.executeQuery()){
//                if(resultSet.next()){
//                    return createProductFromResultSet(resultSet);
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
//    public List<Product> getAllProducts() throws DAOException {
//        List<Product> products = new ArrayList<>();
//        String query = "select * from PRODUCTS";
//        try(Connection conn = connectionPool.getConnection();
//            PreparedStatement statement = conn.prepareStatement(query);
//            ResultSet resultSet = statement.executeQuery()){
//            while(resultSet.next()){
//                products.add(createProductFromResultSet(resultSet));
//            }
//            return products;
//        }
//        catch(DataBaseConnectionException e){
//            throw new DAOException(e.getMessage(), e);
//        }
//        catch(SQLException e){
//            throw new DAOException("unexpected exception", e);
//        }
//    }
//
//    private Product createProductFromResultSet(ResultSet resultSet) throws DAOException {
//        try{
//            int id = resultSet.getInt("ID");
//            String name = resultSet.getString("NAME");
//            double price = resultSet.getDouble("PRICE");
//            int count = resultSet.getInt("COUNT");
//            return new Product(id, name, price, count);
//        }
//        catch(SQLException e){
//            throw new DAOException("unexpected exception", e);
//        }
//    }
//}
