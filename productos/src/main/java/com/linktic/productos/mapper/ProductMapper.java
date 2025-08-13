package com.linktic.productos.mapper;

import com.linktic.productos.dto.product.ProductDTO;
import com.linktic.productos.dto.product.ProductManageDTO;
import com.linktic.productos.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {

    ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    ProductDTO productToProductDto(Product product);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    Product productDtoToProduct(ProductManageDTO productManageDTO);

}
