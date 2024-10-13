package com.local.dao.product;

import com.local.commonexceptions.ApplicationRuntimeException;
import com.local.dao.DAOType;
import com.local.db.ConnectionPool;

public class ProductDAOFactory {
    public static ProductDAO getProductDAO(DAOType type, ConnectionPool connectionPool) {
        switch (type) {
            case MEM -> {
                return new ProductDAOMemImpl();
            }
            case DB -> throw new ApplicationRuntimeException("ProductDAODB not implemented", null);
            default -> throw new ApplicationRuntimeException("unsupported ProductDAO type", null);
        }
    }
}
