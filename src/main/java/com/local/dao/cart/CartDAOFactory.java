package com.local.dao.cart;

import com.local.commonexceptions.ApplicationRuntimeException;
import com.local.dao.DAOType;
import com.local.db.ConnectionPool;

public class CartDAOFactory {
    private static CartDAO cartDAODB;

    public static CartDAO getCartDAO(DAOType daoType, ConnectionPool connectionPool) {
        switch (daoType) {
            case DB -> {
                cartDAODB = new CartDAODBImpl(connectionPool);
                return cartDAODB;
            }
            case MEM -> throw new ApplicationRuntimeException("cartDAOMem not implemented", null);
            default -> throw new ApplicationRuntimeException("unsupported cartDAO type", null);
        }
    }
}
