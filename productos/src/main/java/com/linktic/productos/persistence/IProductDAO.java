package com.linktic.productos.persistence;

import com.linktic.productos.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

public interface IProductDAO {

    Long save(Product product);

    Page<Product> findAll(Pageable pageable);

    Optional<Product> findProductById(Long id);

    void updateProductById(String name, BigDecimal price, Long id);

    void deleteProductById(Long id);
}
