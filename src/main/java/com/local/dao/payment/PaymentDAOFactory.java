package com.local.dao.payment;

import com.local.exception.common.ApplicationRuntimeException;
import com.local.dao.DAOType;
import com.local.dbconnector.ConnectionPool;

public class PaymentDAOFactory {
    private static PaymentDAO paymentDAOMemImpl;
    private static PaymentDAO paymentDAODBImpl;

    public static PaymentDAO getPaymentDAO(DAOType daoType, ConnectionPool connectionPool) {
        switch (daoType) {
            case MEM -> {
                paymentDAOMemImpl = new PaymentDAOMemImpl();
                return paymentDAOMemImpl;
            }
            case DB -> throw new ApplicationRuntimeException("PaymentDAODB not implemented", null);
            default -> throw new ApplicationRuntimeException("unsupported PaymentDAO type", null);
        }
    }
}
