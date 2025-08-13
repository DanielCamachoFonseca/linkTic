package com.linktic.inventarios.persistence.inventory;

import com.linktic.inventarios.entities.Inventory;
import com.linktic.inventarios.persistence.InventoryDAO;
import com.linktic.inventarios.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InventoryDAOImpl implements InventoryDAO {

    @Autowired
    private InventoryRepository inventoryRepository;


    @Override
    public Optional<Inventory> findByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }

    @Override
    public void updateQuantityByProduct(Long id, Long quantity) {
        inventoryRepository.updateQuantityByProduct(id, quantity);
    }
}
