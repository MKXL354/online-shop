package com.local.repository;

import com.local.dto.ProductDto;
import com.local.dto.ProductReportDto;
import com.local.entity.Product;
import com.local.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    @Query("select new com.local.dto.ProductDto(p) from Product p")
    List<ProductDto> findAllDto();

    @Query("select p.name, count(p) as quantity from Product p where p.productStatus = :productStatus group by p.name order by quantity desc")
    List<ProductReportDto> findAllByProductStatus(ProductStatus productStatus);

    Optional<Product> findByNameAndProductStatus(String name, ProductStatus productStatus);
}
