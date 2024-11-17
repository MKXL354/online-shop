package com.local.servlet.user;

import com.local.dao.DAOException;
import com.local.dto.ProductDTO;
import com.local.model.Product;
import com.local.model.User;
import com.local.exception.service.productmanagement.InvalidProductCountException;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.service.user.UserService;
import com.local.service.usermanagement.UserManagementService;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.servlet.mapper.DTOMapper;
import com.local.servlet.mapper.DTOMapperException;
import com.local.servlet.mapper.ProductDTOMapper;
import com.local.servlet.validation.RequiresValidation;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequiresValidation(objectClass = ProductDTO.class, objectName = "productDTO")
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
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            Product product = productDTOMapper.map(productDTO);
            User user = userManagementService.getUserById(userId);
            userService.addProductToCart(user, product);
        }
        catch (DTOMapperException e) {
            throw new ServletException(e.getCause());
        }
        catch (NumberFormatException | UserNotFoundException | InvalidProductCountException | ProductNotFoundException |
               DAOException e) {
            throw new ServletException(e);
        }
    }
}
