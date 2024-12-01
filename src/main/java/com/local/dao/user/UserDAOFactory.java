package com.local.dao.user;

import com.local.exception.common.ApplicationRuntimeException;
import com.local.dao.DAOType;
import com.local.dao.dbconnector.ConnectionPool;
import com.local.util.persistence.PersistenceManager;

public class UserDAOFactory {
    private static UserDAO userDAODB;
    private static UserDAO userDAOMem;

    public static UserDAO getUserDAO(DAOType type, ConnectionPool connectionPool, PersistenceManager persistenceManager) {
        switch(type){
            case MEM -> {
                userDAOMem = persistenceManager.loadData(UserDAOMemImpl.class);
                if(userDAOMem == null){
                    userDAOMem = new UserDAOMemImpl();
                }
                return userDAOMem;
            }
            case DB -> {
                throw new ApplicationRuntimeException("UserDAODB not implemented", null);
//                userDAODB = new UserDAODBImpl(connectionPool);
//                return userDAODB;
            }
            default -> throw new ApplicationRuntimeException("unsupported UserDAO type", null);
        }
    }
}
