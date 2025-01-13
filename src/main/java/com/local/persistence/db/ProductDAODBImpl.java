package com.local.persistence.db;

import com.local.dto.ProductReportDTO;
import com.local.entity.Product;
import com.local.entity.ProductStatus;
import com.local.entity.ProductType;
import com.local.persistence.DAOException;
import com.local.persistence.ProductDAO;
import com.local.persistence.transaction.TransactionManager;
import com.local.persistence.transaction.TransactionManagerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDAODBImpl implements ProductDAO {
    private TransactionManager transactionManager;

    @Autowired
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Product addProduct(Product product) throws DAOException {
        String query = "insert into PRODUCTS(NAME, PRICE, PRODUCT_TYPE, PRODUCT_STATUS) values(?, ?, ?, ?)";
        Connection connection = null;
        try {
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.setString(3, product.getProductType().toString());
            statement.setString(4, product.getProductStatus().toString());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                product.setId(generatedKeys.getInt("ID"));
            }
            return product;
        } catch (TransactionManagerException e) {
            throw new DAOException(e.getMessage(), e);
        } catch (SQLException e) {
            throw new DAOException("constraint violation", e);
        } finally {
            transactionManager.closeConnection(connection);
        }
    }

    @Override
    public void updateProduct(Product product) throws DAOException {
        String query = "update PRODUCTS set NAME = ?, PRICE = ?, PRODUCT_TYPE = ?, PRODUCT_STATUS = ? where ID = ?";
        Connection connection = null;
        try {
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.setString(3, product.getProductType().toString());
            statement.setString(4, product.getProductStatus().toString());
            statement.setInt(5, product.getId());
            statement.executeUpdate();
        } catch (TransactionManagerException e) {
            throw new DAOException(e.getMessage(), e);
        } catch (SQLException e) {
            throw new DAOException("unexpected exception", e);
        } finally {
            transactionManager.closeConnection(connection);
        }
    }

    @Override
    public List<Product> getAllProducts() throws DAOException {
        List<Product> products = new ArrayList<>();
        String query = "select * from PRODUCTS order by ID";
        Connection connection = null;
        try {
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(createProductFromResultSet(resultSet));
            }
            return products;
        } catch (TransactionManagerException e) {
            throw new DAOException(e.getMessage(), e);
        } catch (SQLException e) {
            throw new DAOException("unexpected exception", e);
        } finally {
            transactionManager.closeConnection(connection);
        }
    }

    @Override
    public Product getProductById(int id) throws DAOException {
        String query = "select * from PRODUCTS where ID = ?";
        Connection connection = null;
        try {
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createProductFromResultSet(resultSet);
                } else {
                    return null;
                }
            }
        } catch (TransactionManagerException e) {
            throw new DAOException(e.getMessage(), e);
        } catch (SQLException e) {
            throw new DAOException("unexpected exception", e);
        } finally {
            transactionManager.closeConnection(connection);
        }
    }

    @Override
    public Product getProductByName(String name) throws DAOException {
        String query = "select * from PRODUCTS where NAME = ? and PRODUCT_STATUS = 'AVAILABLE'";
        Connection connection = null;
        try {
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createProductFromResultSet(resultSet);
                } else {
                    return null;
                }
            }
        } catch (TransactionManagerException e) {
            throw new DAOException(e.getMessage(), e);
        } catch (SQLException e) {
            throw new DAOException("unexpected exception", e);
        } finally {
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
    public List<ProductReportDTO> getProductsSortedByStatus(ProductStatus productStatus) throws DAOException {
        List<ProductReportDTO> productsDto = new ArrayList<>();
        String query = "select NAME, PRICE, PRODUCT_TYPE, COUNT(*) as COUNT from PRODUCTS where PRODUCT_STATUS = ? group by NAME order by COUNT desc";
        Connection connection = null;
        try {
            connection = transactionManager.openConnection();
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, productStatus.toString());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = 0;
                String name = resultSet.getString("NAME");
                BigDecimal price = resultSet.getBigDecimal("PRICE");
                ProductType productType = ProductType.valueOf(resultSet.getString("PRODUCT_TYPE"));
                int count = resultSet.getInt("COUNT");
                Product product = new Product(id, name, price, productType, productStatus);
                productsDto.add(new ProductReportDTO(product, count));
            }
            return productsDto;
        } catch (TransactionManagerException e) {
            throw new DAOException(e.getMessage(), e);
        } catch (SQLException e) {
            throw new DAOException("unexpected exception", e);
        } finally {
            transactionManager.closeConnection(connection);
        }
    }
}
