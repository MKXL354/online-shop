package com.local.servlet.usermanagement;

import com.local.dao.DAOException;
import com.local.model.User;
import com.local.service.usermanagement.DuplicateUsernameException;
import com.local.service.usermanagement.UserManagementService;
import com.local.servlet.CommonWebComponentService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AddUserServlet extends HttpServlet {
    private UserManagementService userManagementService;
    private CommonWebComponentService commonWebComponentService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userManagementService = (UserManagementService)getServletContext().getAttribute("userManagementService");
        commonWebComponentService = (CommonWebComponentService)getServletContext().getAttribute("commonWebComponentService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = (User)request.getAttribute("user");
        try {
            user = userManagementService.addUser(user);
            commonWebComponentService.writeResponse(response, user);
        } catch (DuplicateUsernameException | DAOException e) {
            throw new ServletException(e);
        }
    }
}
