package com.local.dao.product;

import com.local.commonexceptions.ApplicationRuntimeException;
import com.local.dao.DAOType;
import com.local.db.ConnectionPool;

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
                productDAODB = new ProductDAODBImpl(connectionPool);
                return productDAODB;
            }
            default -> throw new ApplicationRuntimeException("unsupported ProductDAO type", null);
        }
    }
}
