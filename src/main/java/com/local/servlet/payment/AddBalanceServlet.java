package com.local.servlet.payment;

import com.local.dao.DAOException;
import com.local.service.payment.PaymentService;
import com.local.exception.service.usermanagement.UserNotFoundException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;

public class AddBalanceServlet extends HttpServlet {
    private PaymentService paymentService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        paymentService = (PaymentService)getServletContext().getAttribute("paymentService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try{
            int userId = (int)request.getAttribute("userId");
            BigDecimal amount = new BigDecimal(request.getParameter("amount"));
            paymentService.addBalance(userId, amount);
        }
        catch(NumberFormatException | UserNotFoundException | DAOException e){
            throw new ServletException(e);
        }
    }
}
