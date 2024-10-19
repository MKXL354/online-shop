package com.local.servlet.cartmanagement;

import com.local.commonexceptions.ApplicationException;
import com.local.dto.ProductDTO;
import com.local.model.Cart;
import com.local.model.Product;
import com.local.service.cartmanagement.CartManagementService;
import com.local.servlet.mapper.DTOMapper;
import com.local.servlet.mapper.ProductDTOMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AddProductToCartServlet extends HttpServlet {
    private DTOMapper<ProductDTO, Product> productDTOMapper;
    private CartManagementService cartManagementService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDTOMapper = (ProductDTOMapper)getServletContext().getAttribute("productDTOMapper");
        cartManagementService = (CartManagementService)getServletContext().getAttribute("cartManagementService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        ProductDTO productDTO = (ProductDTO)request.getAttribute("productDTO");
        try {
            Product product = productDTOMapper.map(productDTO);
            Cart cart = new Cart(1, null, null);
            cartManagementService.addProductToCart(cart, product);
        } catch (ApplicationException e) {
            throw new ServletException(e);
        }
    }
}
//TODO: get active cart of the user. pass id in productDTO?
