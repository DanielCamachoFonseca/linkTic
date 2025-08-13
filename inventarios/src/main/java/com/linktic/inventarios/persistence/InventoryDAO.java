package com.linktic.inventarios.persistence;

import com.linktic.inventarios.entities.Inventory;

import java.util.Optional;

public interface InventoryDAO {

    Optional<Inventory> findByProductId(Long productId);

    void updateQuantityByProduct(Long id, Long quantity);
}
