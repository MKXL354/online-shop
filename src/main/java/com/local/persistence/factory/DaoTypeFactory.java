package com.local.persistence.factory;

import com.local.exception.common.ApplicationRuntimeException;
import org.springframework.stereotype.Component;

@Component
public class DaoTypeFactory {
    public static DAOFactory getFactory(DAOType daoType) {
        switch (daoType) {
            case DB -> {
                return new DBDAOFactory();
            }
            case MEM -> throw new ApplicationRuntimeException("MemDAOType not implemented", null);
            default -> throw new ApplicationRuntimeException("invalid DAOType: " + daoType, null);
        }
    }
}
