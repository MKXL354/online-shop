package com.local.servlet.payment;

import com.local.dao.DAOException;
import com.local.model.Payment;
import com.local.service.common.CommonService;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.servlet.common.CommonWebComponentService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class GetPendingPaymentServlet extends HttpServlet {
    private CommonService commonService;
    private CommonWebComponentService commonWebComponentService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        commonService = (CommonService)getServletContext().getAttribute("commonService");
        commonWebComponentService = (CommonWebComponentService)getServletContext().getAttribute("commonWebComponentService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            int userId = (int)request.getAttribute("userId");
            Payment payment = commonService.getPendingPayment(userId);
            commonWebComponentService.writeResponse(response, payment);
        }
        catch(NumberFormatException | UserNotFoundException | DAOException e){
            throw new ServletException(e);
        }
    }
}
