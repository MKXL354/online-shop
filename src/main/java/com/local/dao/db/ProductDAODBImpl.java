package com.local.dao.db;

import com.local.dao.DAOException;
import com.local.dao.ProductDAO;
import com.local.dao.transaction.TransactionManager;
import com.local.dao.transaction.TransactionManagerException;
import com.local.model.Product;
import com.local.model.ProductStatus;
import com.local.model.ProductType;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class ProductDAODBImpl implements ProductDAO {
    private TransactionManager transactionManager;

    public ProductDAODBImpl(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Product addProduct(Product product) throws DAOException {
        String query = "insert into PRODUCTS(NAME, PRICE, PRODUCT_TYPE, PRODUCT_STATUS) values(?, ?, ?, ?)";
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.setString(3, product.getProductType().toString());
            statement.setString(4, product.getProductStatus().toString());
            statement.executeUpdate();
            try(ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                product.setId(generatedKeys.getInt("ID"));
            }
            return product;
        }
        catch(TransactionManagerException e){
            throw new DAOException(e.getMessage(), e);
        }
        catch(SQLException e){
            throw new DAOException("constraint violation", e);
        }
        finally {
            transactionManager.closeConnection(connection);
        }
    }

    @Override
    public void updateProduct(Product product) throws DAOException {
        String query = "update PRODUCTS set NAME = ?, PRICE = ?, PRODUCT_TYPE = ?, PRODUCT_STATUS = ? where ID = ?";
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.setString(3, product.getProductType().toString());
            statement.setString(4, product.getProductStatus().toString());
            statement.setInt(5, product.getId());
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
    public HashSet<Product> getAllProducts() throws DAOException {
        HashSet<Product> products = new HashSet<>();
        String query = "select * from PRODUCTS";
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                products.add(createProductFromResultSet(resultSet));
            }
            return products;
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
    public Product getProductById(int id) throws DAOException {
        return getProductByIdentifier("ID", id, JDBCType.INTEGER);
    }

    @Override
    public Product getProductByName(String name) throws DAOException {
        return getProductByIdentifier("NAME", name, JDBCType.VARCHAR);
    }

    private Product getProductByIdentifier(String columnName, Object value, SQLType type) throws DAOException {
        String query = String.format("select * from PRODUCTS where %s = ?", columnName);
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setObject(1, value, type);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    return createProductFromResultSet(resultSet);
                }
                else{
                    return null;
                }
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

    private Product createProductFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("ID");
        String name = resultSet.getString("NAME");
        BigDecimal price = resultSet.getBigDecimal("PRICE");
        ProductType productType = ProductType.valueOf(resultSet.getString("PRODUCT_TYPE"));
        ProductStatus productStatus = ProductStatus.valueOf(resultSet.getString("PRODUCT_STATUS"));
        return new Product(id, name, price, productType, productStatus);
    }

    @Override
    public LinkedHashMap<String, Integer> getProductsSortedBySold() throws DAOException {
        return getProductsSortedByStatus(ProductStatus.SOLD);
    }

    @Override
    public LinkedHashMap<String, Integer> getProductsSortedByAvailable() throws DAOException {
        return getProductsSortedByStatus(ProductStatus.AVAILABLE);
    }

    private LinkedHashMap<String, Integer> getProductsSortedByStatus(ProductStatus productStatus) throws DAOException {
        LinkedHashMap<String, Integer> products = new LinkedHashMap<>();
        String query = "select NAME, COUNT(*) as SOLD from PRODUCTS where PRODUCT_STATUS = ? group by NAME order by SOLD desc";
        Connection connection = null;
        try{
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, productStatus.toString());
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                String name = resultSet.getString("NAME");
                int count = resultSet.getInt(productStatus.toString());
                products.put(name, count);
            }
            return products;
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
