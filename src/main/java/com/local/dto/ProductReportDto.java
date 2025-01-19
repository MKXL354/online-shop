package com.local.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;

public class ProductReportDto {
    @NotEmpty
    private String productName;

    @PositiveOrZero
    private long count;

    public ProductReportDto(String productName, long count) {
        this.productName = productName;
        this.count = count;
    }

    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getCount() {
        return count;
    }
    
    public void setCount(long count) {
        this.count = count;
    }
}
