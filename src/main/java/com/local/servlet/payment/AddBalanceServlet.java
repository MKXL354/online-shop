package com.local.servlet.payment;

import com.local.dao.DAOException;
import com.local.model.User;
import com.local.service.payment.PaymentService;
import com.local.service.usermanagement.UserManagementService;
import com.local.exception.service.usermanagement.UserNotFoundException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;

public class AddBalanceServlet extends HttpServlet {
    private UserManagementService userManagementService;
    private PaymentService paymentService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userManagementService = (UserManagementService)getServletContext().getAttribute("userManagementService");
        paymentService = (PaymentService)getServletContext().getAttribute("paymentService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try{
            int userId = Integer.parseInt(request.getParameter("userId"));
            BigDecimal amount = new BigDecimal(request.getParameter("amount"));
            User user = userManagementService.getUserById(userId);
            paymentService.addBalance(user, amount);
        }
        catch(NumberFormatException | UserNotFoundException | DAOException e){
            throw new ServletException(e);
        }
    }
}
