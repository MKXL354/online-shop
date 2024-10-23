package com.local.dao.cart;

import com.local.commonexceptions.ApplicationRuntimeException;
import com.local.dao.DAOType;
import com.local.db.ConnectionPool;

public class CartDAOFactory {
    private static CartDAO cartDAODB;
    private static CartDAO cartDAOMem;

    public static CartDAO getCartDAO(DAOType daoType, ConnectionPool connectionPool) {
        switch (daoType) {
            case DB -> {
                throw new ApplicationRuntimeException("cartDAODB not implemented", null);
//                cartDAODB = new CartDAODBImpl(connectionPool);
//                return cartDAODB;
            }
            case MEM -> {
                cartDAOMem = new CartDAOMemImpl();
                return cartDAOMem;
            }
            default -> throw new ApplicationRuntimeException("unsupported cartDAO type", null);
        }
    }
}
