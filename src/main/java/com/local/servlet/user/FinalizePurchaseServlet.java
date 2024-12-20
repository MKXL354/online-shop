package com.local.servlet.user;

import com.local.dao.DAOException;
import com.local.exception.service.user.EmptyCartException;
import com.local.service.user.UserService;
import com.local.exception.service.usermanagement.UserNotFoundException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FinalizePurchaseServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userService = (UserService)getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try{
            int userId = (int)request.getAttribute("userId");
            userService.finalizePurchase(userId);
        }
        catch(NumberFormatException | UserNotFoundException | EmptyCartException |
              DAOException e){
            throw new ServletException(e);
        }
    }
}
