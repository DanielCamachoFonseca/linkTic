package com.linktic.inventarios.repository;

import com.linktic.inventarios.entities.Inventory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface InventoryRepository extends CrudRepository<Inventory, Long> {

    Optional<Inventory> findByProductId(Long productId);

    @Modifying
    @Query("UPDATE Inventory l SET l.quantity = :quantity WHERE l.id = :id")
    void updateQuantityByProduct(Long id, Long quantity);
}
