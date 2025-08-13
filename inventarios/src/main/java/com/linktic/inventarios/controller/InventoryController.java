package com.linktic.inventarios.controller;

import com.linktic.inventarios.dto.inventory.InventoryProductDTO;
import com.linktic.inventarios.dto.inventory.UpdateInventoryDTO;
import com.linktic.inventarios.dto.product.RequestSearchProductDTO;
import com.linktic.inventarios.dto.purchase.PurchaseRequestDTO;
import com.linktic.inventarios.dto.purchase.PurchaseResponseDTO;
import com.linktic.inventarios.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@Tag(name = "Manejo de inventarios", description = "Gestion de operaciones sobre los inventarios")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/getInventoryByProductId")
    @Operation(summary = "Obtener cantidad disponible de un producto por id")
    public ResponseEntity<InventoryProductDTO> getInventoryByProductId(@RequestBody RequestSearchProductDTO request) {
        return ResponseEntity.ok(inventoryService.getInventoryByProductId(request));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Actualiza cantidad disponible de un producto")
    public ResponseEntity<?> updateQuantityByProduct(@PathVariable Long id, @RequestBody UpdateInventoryDTO updateInventoryDTO) {
        inventoryService.updateQuantityByProduct(updateInventoryDTO, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/purchase")
    @Operation(summary = "Compra de productos")
    public ResponseEntity<?> purchaseProduct(@RequestBody PurchaseRequestDTO request) {
        try {
            PurchaseResponseDTO response = inventoryService.purchaseProduct(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}