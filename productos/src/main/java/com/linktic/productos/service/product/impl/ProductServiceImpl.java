package com.linktic.productos.service.product.impl;

import com.linktic.productos.dto.product.ProductDTO;
import com.linktic.productos.dto.product.ProductManageDTO;
import com.linktic.productos.entities.Product;
import com.linktic.productos.exception.RequestException;
import com.linktic.productos.mapper.ProductMapper;
import com.linktic.productos.persistence.IProductDAO;
import com.linktic.productos.service.product.IProductService;
import com.linktic.productos.utilities.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductDAO productDAO;

    @Override
    public Long save(ProductManageDTO productManageDTO) {
        log.info("Guardando producto: {}", productManageDTO);
        Product product = ProductMapper.mapper.productDtoToProduct(productManageDTO);
        Long id = productDAO.save(product);
        log.info("Producto guardado con ID: {}", id);
        return id;
    }

    @Override
    public Page<ProductDTO> getProduct(Pageable pageable) {
        log.info("Obteniendo lista de productos con paginación: {}", pageable);
        Page<Product> productEntitiesPage = productDAO.findAll(pageable);

        List<ProductDTO> productDTOs = productEntitiesPage.getContent().stream()
                .map(ProductMapper.mapper::productToProductDto)
                .collect(Collectors.toList());

        log.info("Productos obtenidos: {}", productDTOs.size());
        return new PageImpl<>(productDTOs, productEntitiesPage.getPageable(), productEntitiesPage.getTotalElements());
    }

    @Override
    public Optional<Product> findProductById(Long id) {
        log.info("Buscando producto con ID: {}", id);
        Optional<Product> product = productDAO.findProductById(id);
        if (product.isPresent()) {
            log.info("Producto encontrado: {}", product.get());
        } else {
            log.warn("Producto con ID {} no encontrado", id);
        }
        return product;
    }

    @Override
    @Transactional
    public void updateProductById(ProductManageDTO productManageDTO, Long id) {
        log.info("Actualizando producto con ID: {} con datos: {}", id, productManageDTO);
        Optional<Product> productOptional = findProductById(id);
        if (productOptional.isPresent()) {
            productDAO.updateProductById(productManageDTO.getName(), productManageDTO.getPrice(), id);
            log.info("Producto con ID {} actualizado correctamente", id);
        } else {
            log.error("Error al actualizar: el producto con ID {} no existe", id);
            throw new RequestException("500", AppConstants.ERROR_MESSAGE_PRODUCT_NOT_FOUND);
        }
    }

    @Override
    public void deleteProductById(Long id) {
        log.info("Eliminando producto con ID: {}", id);
        Optional<Product> productOptional = findProductById(id);
        if (productOptional.isPresent()) {
            productDAO.deleteProductById(productOptional.get().getId());
            log.info("Producto con ID {} eliminado correctamente", id);
        } else {
            log.error("Error al eliminar: el producto con ID {} no existe", id);
            throw new RequestException("500", AppConstants.ERROR_MESSAGE_PRODUCT_NOT_FOUND);
        }
    }
}
