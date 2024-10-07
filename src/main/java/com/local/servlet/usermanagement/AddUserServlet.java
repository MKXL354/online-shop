package com.local.servlet.usermanagement;

import com.local.model.User;
import com.local.service.UserManagementServiceException;
import com.local.service.UserManagementService;
import com.local.servlet.CommonServletService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AddUserServlet extends HttpServlet {
    private UserManagementService userManagementService;
    private CommonServletService commonServletService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userManagementService = (UserManagementService)getServletContext().getAttribute("userManagementService");
        commonServletService = (CommonServletService)getServletContext().getAttribute("commonServletServices");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User)request.getAttribute("user");
        try {
            userManagementService.addUser(user);
            commonServletService.writeResponse(response, "success");
        } catch (UserManagementServiceException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            commonServletService.writeResponse(response, e.getMessage());
        }
    }
}
