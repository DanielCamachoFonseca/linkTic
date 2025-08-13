package com.linktic.inventarios.service.impl;

import com.linktic.inventarios.client.ProductClient;
import com.linktic.inventarios.dto.inventory.InventoryProductDTO;
import com.linktic.inventarios.dto.inventory.UpdateInventoryDTO;
import com.linktic.inventarios.dto.product.ProductDTO;
import com.linktic.inventarios.dto.product.RequestSearchProductDTO;
import com.linktic.inventarios.dto.purchase.PurchaseRequestDTO;
import com.linktic.inventarios.dto.purchase.PurchaseResponseDTO;
import com.linktic.inventarios.entities.Inventory;
import com.linktic.inventarios.exception.BusinessException;
import com.linktic.inventarios.persistence.InventoryDAO;
import com.linktic.inventarios.service.InventoryService;
import com.linktic.inventarios.utilities.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Retryable(
        value = { ResourceAccessException.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000)
)
@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryDAO inventoryDAO;
    private final RestTemplate restTemplate;
    private final ProductClient productClient;

    public InventoryServiceImpl(InventoryDAO inventoryPersistence, RestTemplate restTemplate, ProductClient productClient) {
        this.inventoryDAO = inventoryPersistence;
        this.restTemplate = restTemplate;
        this.productClient = productClient;
    }

    @Override
    public InventoryProductDTO getInventoryByProductId(RequestSearchProductDTO request) {
        log.info("[getInventoryByProductId] Iniciando búsqueda de inventario para producto ID: {}", request.getProductId());

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", AppConstants.PRIVATE_KEY);

        String url = AppConstants.URL_MICROSERVICES_PRODUCT + "/findProductById/" + request.getProductId();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        log.debug("[getInventoryByProductId] Llamando a servicio de productos en URL: {}", url);
        ResponseEntity<ProductDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                ProductDTO.class
        );

        ProductDTO product = response.getBody();
        log.debug("[getInventoryByProductId] Respuesta de servicio de productos: {}", product);

        if (product == null) {
            log.error("[getInventoryByProductId] Producto no encontrado en microservicio productos");
            throw new RuntimeException(AppConstants.ERROR_MESSAGE_PRODUCT_NOT_FOUND);
        }

        Inventory inventory = inventoryDAO.findByProductId(request.getProductId())
                .orElseThrow(() -> {
                    log.error("[getInventoryByProductId] No hay inventario para este producto");
                    return new RuntimeException(AppConstants.ERROR_MESSAGE_INVENTARY_NOT_FOUND);
                });

        log.info("[getInventoryByProductId] Inventario encontrado: {}", inventory);

        return InventoryProductDTO.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productPrice(product.getPrice())
                .quantity(inventory.getQuantity())
                .build();
    }

    @Override
    public Optional<Inventory> findInventoryById(Long id) {
        log.debug("[findInventoryById] Buscando inventario por ID de producto: {}", id);
        return inventoryDAO.findByProductId(id);
    }

    @Override
    @Transactional
    public void updateQuantityByProduct(UpdateInventoryDTO updateInventoryDTO, Long id) {
        log.info("[updateQuantityByProduct] Actualizando inventario para producto ID: {}", id);
        Optional<Inventory> inventoryOptional = findInventoryById(id);

        if (inventoryOptional.isPresent()) {
            inventoryDAO.updateQuantityByProduct(id, updateInventoryDTO.getQuantity());
            log.info("[updateQuantityByProduct] Inventario actualizado con nueva cantidad: {}", updateInventoryDTO.getQuantity());
        } else {
            log.error("[updateQuantityByProduct] El inventario para el producto ID {} no existe", id);
            throw new RuntimeException(AppConstants.ERROR_MESSAGE_INVENTARY_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public PurchaseResponseDTO purchaseProduct(PurchaseRequestDTO purchaseRequestDTO) {
        log.info("[purchaseProduct] Iniciando proceso de compra para producto ID: {}", purchaseRequestDTO.getProductId());

        ProductDTO product = productClient.getProductById(purchaseRequestDTO.getProductId(), AppConstants.PRIVATE_KEY);
        log.debug("[purchaseProduct] Producto obtenido del servicio externo: {}", product);

        if (product == null) {
            log.error("[purchaseProduct] Producto no encontrado");
            throw new RuntimeException(AppConstants.ERROR_MESSAGE_PRODUCT_NOT_FOUND);
        }

        Inventory inventory = inventoryDAO.findByProductId(purchaseRequestDTO.getProductId())
                .orElseThrow(() -> {
                    log.error("[purchaseProduct] Inventario no encontrado para producto ID {}", purchaseRequestDTO.getProductId());
                    return new RuntimeException((AppConstants.ERROR_MESSAGE_INVENTARY_NOT_FOUND));
                });

        if (inventory.getQuantity() < purchaseRequestDTO.getQuantity()) {
            log.warn("[purchaseProduct] Inventario insuficiente. Disponible: {}, Solicitado: {}",
                    inventory.getQuantity(), purchaseRequestDTO.getQuantity());
            throw new BusinessException(AppConstants.INSUFFICIENT_INVENTARY);
        }

        log.info("[purchaseProduct] Inventario actual: {}. Restando cantidad solicitada: {}",
                inventory.getQuantity(), purchaseRequestDTO.getQuantity());

        inventory.setQuantity(inventory.getQuantity() - purchaseRequestDTO.getQuantity());
        inventoryDAO.updateQuantityByProduct(purchaseRequestDTO.getProductId(), inventory.getQuantity());

        Double totalPrice = product.getPrice()
                .multiply(BigDecimal.valueOf(purchaseRequestDTO.getQuantity()))
                .doubleValue();

        PurchaseResponseDTO response = new PurchaseResponseDTO(
                product.getId(),
                product.getName(),
                Math.toIntExact(purchaseRequestDTO.getQuantity()),
                product.getPrice().doubleValue(),
                totalPrice,
                AppConstants.SUCCESS_PURCHASE
        );

        log.info("[purchaseProduct] Compra procesada con éxito: {}", response);
        return response;
    }
}
