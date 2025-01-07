package com.local.dto;

import com.local.model.Product;

public class ProductReportDTO {
    private Product product;
    private int count;

    public ProductReportDTO(Product product, int count) {
        this.product = product;
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public int getCount() {
        return count;
    }
}
