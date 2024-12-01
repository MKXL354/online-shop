package com.local.dao.cart;

import com.local.exception.common.ApplicationRuntimeException;
import com.local.dao.DAOType;
import com.local.dao.dbconnector.ConnectionPool;
import com.local.util.persistence.PersistenceManager;

public class CartDAOFactory {
    private static CartDAO cartDAODB;
    private static CartDAO cartDAOMem;

    public static CartDAO getCartDAO(DAOType daoType, ConnectionPool connectionPool, PersistenceManager persistenceManager) {
        switch (daoType) {
            case DB -> {
                throw new ApplicationRuntimeException("cartDAODB not implemented", null);
//                cartDAODB = new CartDAODBImpl(connectionPool);
//                return cartDAODB;
            }
            case MEM -> {
                cartDAOMem = persistenceManager.loadData(CartDAOMemImpl.class);
                if(cartDAOMem == null){
                    cartDAOMem = new CartDAOMemImpl();
                }
                return cartDAOMem;
            }
            default -> throw new ApplicationRuntimeException("unsupported cartDAO type", null);
        }
    }
}
