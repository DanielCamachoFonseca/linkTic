package com.linktic.productos.repository.product;

import com.linktic.productos.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Page<Product> findAllByOrderByIdAsc(Pageable pageable);

    @Query("SELECT l FROM Product l WHERE l.id = :productId ORDER BY l.id ASC")
    Product findProductById(Long productId);

    @Modifying
    @Query("UPDATE Product l SET l.name = :name, l.price = :price WHERE l.id = :id")
    void updateProductById(String name, BigDecimal price, Long id);

}
