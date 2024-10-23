package com.local.servlet.userservice;

import com.local.commonexceptions.ApplicationException;
import com.local.dto.ProductDTO;
import com.local.model.Cart;
import com.local.model.Product;
import com.local.model.User;
import com.local.service.user.UserService;
import com.local.service.usermanagement.UserManagementService;
import com.local.servlet.mapper.DTOMapper;
import com.local.servlet.mapper.ProductDTOMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AddProductToCartServlet extends HttpServlet {
    private DTOMapper<ProductDTO, Product> productDTOMapper;
    private UserManagementService userManagementService;
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDTOMapper = (ProductDTOMapper)getServletContext().getAttribute("productDTOMapper");
        userManagementService = (UserManagementService)getServletContext().getAttribute("userManagementService");
        userService = (UserService)getServletContext().getAttribute("userService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        ProductDTO productDTO = (ProductDTO)request.getAttribute("productDTO");
        int userId = Integer.parseInt(request.getParameter("userId"));
        try {
            Product product = productDTOMapper.map(productDTO);
            User user = userManagementService.getUserById(userId);
            Cart cart = userService.getCart(user);
            userService.addProductToCart(cart, product);
        } catch (ApplicationException e) {
            throw new ServletException(e);
        }
    }
}
//TODO: userId int validation
