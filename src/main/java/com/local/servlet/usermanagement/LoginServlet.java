package com.local.servlet.usermanagement;

import com.local.dao.DAOException;
import com.local.model.User;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.exception.service.usermanagement.WrongPasswordException;
import com.local.util.token.TokenManager;
import com.local.service.usermanagement.UserManagementService;
import com.local.servlet.common.CommonWebComponentService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    private UserManagementService userManagementService;
    private CommonWebComponentService commonWebComponentService;
    private TokenManager tokenManager;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userManagementService = (UserManagementService)getServletContext().getAttribute("userManagementService");
        commonWebComponentService = (CommonWebComponentService)getServletContext().getAttribute("commonWebComponentService");
        tokenManager = (TokenManager)getServletContext().getAttribute("tokenManager");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            User user = userManagementService.login(username, password);

            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getType().toString());
            claims.put("userId", user.getId());
            String jws = tokenManager.getSignedToken(claims);

            response.setHeader("Authorization", jws);
            commonWebComponentService.writeResponse(response, user);
        } catch (UserNotFoundException | WrongPasswordException | DAOException e) {
            throw new ServletException(e);
        }
    }
}
