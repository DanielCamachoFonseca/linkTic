package com.linktic.inventarios.service;

import com.linktic.inventarios.dto.inventory.InventoryProductDTO;
import com.linktic.inventarios.dto.inventory.UpdateInventoryDTO;
import com.linktic.inventarios.dto.product.RequestSearchProductDTO;
import com.linktic.inventarios.dto.purchase.PurchaseRequestDTO;
import com.linktic.inventarios.dto.purchase.PurchaseResponseDTO;
import com.linktic.inventarios.entities.Inventory;

import java.util.Optional;

public interface InventoryService {

    InventoryProductDTO getInventoryByProductId(RequestSearchProductDTO request);

    Optional<Inventory> findInventoryById(Long id);

    void updateQuantityByProduct(UpdateInventoryDTO updateInventoryDTO, Long id);

    PurchaseResponseDTO purchaseProduct(PurchaseRequestDTO purchaseRequestDTO);
}
