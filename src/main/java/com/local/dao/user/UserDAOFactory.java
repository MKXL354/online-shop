package com.local.dao.user;

import com.local.commonexceptions.ApplicationRuntimeException;
import com.local.dao.DAOType;
import com.local.db.ConnectionPool;

public class UserDAOFactory {
    public static UserDAO getUserDAO(DAOType type, ConnectionPool connectionPool) {
        switch(type){
            case MEM -> throw new ApplicationRuntimeException("UserDAOMem not implemented", null);
            case DB -> {
                return new UserDAODBImpl(connectionPool);
            }
            default -> throw new ApplicationRuntimeException("unsupported UserDAO type", null);
        }
    }
}
