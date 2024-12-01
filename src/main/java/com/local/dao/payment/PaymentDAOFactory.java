package com.local.dao.payment;

import com.local.exception.common.ApplicationRuntimeException;
import com.local.dao.DAOType;
import com.local.dbconnector.ConnectionPool;
import com.local.util.persistence.PersistenceManager;

public class PaymentDAOFactory {
    private static PaymentDAO paymentDAOMemImpl;
    private static PaymentDAO paymentDAODBImpl;

    public static PaymentDAO getPaymentDAO(DAOType daoType, ConnectionPool connectionPool, PersistenceManager persistenceManager) {
        switch (daoType) {
            case MEM -> {
                paymentDAOMemImpl = persistenceManager.loadData(PaymentDAOMemImpl.class);
                if(paymentDAOMemImpl == null){
                    paymentDAOMemImpl = new PaymentDAOMemImpl();
                }
                return paymentDAOMemImpl;
            }
            case DB -> throw new ApplicationRuntimeException("PaymentDAODB not implemented", null);
            default -> throw new ApplicationRuntimeException("unsupported PaymentDAO type", null);
        }
    }
}
