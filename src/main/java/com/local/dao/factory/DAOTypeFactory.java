package com.local.dao.factory;

import com.local.dao.transaction.TransactionManager;
import com.local.exception.common.ApplicationRuntimeException;

public class DAOTypeFactory {
    private DAOFactory dbFactory;

    public DAOFactory getDbFactory(DAOType type, TransactionManager transactionManager) {
        switch (type) {
            case DB -> {
                return new DBDAOFactory(transactionManager);
            }
            case MEM -> throw new ApplicationRuntimeException("MemDAOType not implemented", null);
            default -> throw new ApplicationRuntimeException("invalid DAOType: " + type, null);
        }
    }
}
