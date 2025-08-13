package com.linktic.inventarios.client;

import com.linktic.inventarios.dto.product.ProductDTO;
import com.linktic.inventarios.utilities.AppConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "productos-client",
        url = AppConstants.URL_MICROSERVICES_PRODUCT
)
public interface ProductClient {

    @GetMapping("/findProductById/{id}")
    ProductDTO getProductById(
            @PathVariable("id") Long id,
            @RequestHeader("X-API-KEY") String apiKey
    );
}