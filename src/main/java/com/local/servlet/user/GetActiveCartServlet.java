package com.local.servlet.user;

import com.local.dao.DAOException;
import com.local.model.Cart;
import com.local.service.common.CommonService;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.servlet.common.CommonWebComponentService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class GetActiveCartServlet extends HttpServlet {
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
        try {
            int userId = (int)request.getAttribute("userId");
            Cart cart =  commonService.getActiveCart(userId);
            commonWebComponentService.writeResponse(response, cart);
        }
        catch (NumberFormatException | UserNotFoundException | DAOException e) {
            throw new ServletException(e);
        }
    }
}

