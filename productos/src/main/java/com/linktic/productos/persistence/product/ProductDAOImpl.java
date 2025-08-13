package com.linktic.productos.persistence.product;

import com.linktic.productos.entities.Product;
import com.linktic.productos.persistence.IProductDAO;
import com.linktic.productos.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class ProductDAOImpl implements IProductDAO {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Long save(Product product) {
        Product productReturn =productRepository.save(product);
        return productReturn.getId();
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAllByOrderByIdAsc(pageable);
    }

    @Override
    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void updateProductById(String name, BigDecimal price, Long id) {
        productRepository.updateProductById(name, price, id);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }
}
