package com.linktic.productos.controller;

import com.linktic.productos.dto.product.ProductDTO;
import com.linktic.productos.dto.product.ProductManageDTO;
import com.linktic.productos.entities.Product;
import com.linktic.productos.exception.RequestException;
import com.linktic.productos.service.product.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
@RequestMapping("/product")
@Tag(name = "Manejo de productos", description = "Gestion de operaciones sobre los productos")
public class ProductController {

    @Autowired
    private IProductService productService;

    @PostMapping("/save")
    @Operation(summary = "Creación de productos")
    public ResponseEntity<?> save(@RequestBody ProductManageDTO ProductManageDTO) throws URISyntaxException {

        if (StringUtils.isBlank(ProductManageDTO.getName())) {
            throw new RequestException("500", "Nombre es requerido");
        }

        if (ProductManageDTO.getPrice() == null) {
            throw new RequestException("500", "Precio es requerido");
        }

        Long ProductId = productService.save(ProductManageDTO);
        URI location = new URI("/product/save");
        return ResponseEntity.created(location).body(ProductId);
    }

    @GetMapping("/findProductById/{productId}")
    @Operation(summary = "Obtiene productos por id")
    private ResponseEntity<?> findProductById(@PathVariable Long productId) {
        Optional<Product> product = productService.findProductById(productId);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/findAll")
    @Operation(summary = "Obtiene todos los productos con paginación")
    private ResponseEntity<Page<ProductDTO>> findAll(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(productService.getProduct(pageable), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Actualiza productos")
    public ResponseEntity<?> updateProductById(@PathVariable Long id, @RequestBody ProductManageDTO productManageDTO) {
        productService.updateProductById(productManageDTO, id);
        return ResponseEntity.ok().build();
    }

}
