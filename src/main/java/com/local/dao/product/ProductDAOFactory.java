package com.local.dao.product;

import com.local.exception.common.ApplicationRuntimeException;
import com.local.dao.DAOType;
import com.local.dbconnector.ConnectionPool;

public class ProductDAOFactory {
    private static ProductDAO productDAOMem;
    private static ProductDAO productDAODB;

    public static ProductDAO getProductDAO(DAOType type, ConnectionPool connectionPool) {
        switch (type) {
            case MEM -> {
                productDAOMem = new ProductDAOMemImpl();
                return productDAOMem;
            }
            case DB -> {
                throw new ApplicationRuntimeException("ProductDAODB not implemented", null);
//                productDAODB = new ProductDAODBImpl(connectionPool);
//                return productDAODB;
            }
            default -> throw new ApplicationRuntimeException("unsupported ProductDAO type", null);
        }
    }
}
