package com.local.dto;

public class ProductDTO {
    private int id;
    private int count;

    public ProductDTO(int id, int count) {
        this.id = id;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public int getCount() {
        return count;
    }
}
