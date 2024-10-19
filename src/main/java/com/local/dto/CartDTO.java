package com.local.dto;

import com.local.util.objectvalidator.NotNull;

import java.util.HashSet;

public class CartDTO {
    private int id;
    private int userId;

    @NotNull(message = "products cant be null")
    private HashSet<ProductDTO> products;

    public CartDTO(int id, int userId, HashSet<ProductDTO> products) {
        this.id = id;
        this.userId = userId;
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public HashSet<ProductDTO> getProducts() {
        return products;
    }
}
