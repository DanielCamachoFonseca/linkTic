package com.linktic.productos.service.product;

import com.linktic.productos.dto.product.ProductDTO;
import com.linktic.productos.dto.product.ProductManageDTO;
import com.linktic.productos.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    /**
     * Metodo que guarda un producto
     * @param productManageDTO
     * @return
     */
    Long save(ProductManageDTO productManageDTO);

    /**
     * Metodo que obtiene lista de productos con paginación
     * @param pageable
     * @return
     */
    Page<ProductDTO> getProduct(Pageable pageable);

    /**
     * Metodo que obtiene un producto por id
     * @param id
     * @return
     */
    Optional<Product> findProductById(Long id);

    /**
     * Metodo que actualiza un producto por id
     * @param productManageDTO
     * @param id
     */
    void updateProductById(ProductManageDTO productManageDTO, Long id);

    /**
     * Metodo que elimina un producto por id
     * @param id
     */
    void deleteProductById(Long id);

}
