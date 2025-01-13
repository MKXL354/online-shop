package com.local.repository;

import com.local.dto.ProductReportDto;
import com.local.entity.Product;
import com.local.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    @Query("select p.name, count(p) as quantity from Product p where p.productStatus = :productStatus group by p.name order by quantity desc")
    List<ProductReportDto> findAllByProductStatus(ProductStatus productStatus);
}
