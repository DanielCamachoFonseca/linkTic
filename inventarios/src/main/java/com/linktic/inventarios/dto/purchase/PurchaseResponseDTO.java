package com.linktic.inventarios.dto.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseResponseDTO {
    private Long productId;
    private String productName;
    private Integer quantityPurchased;
    private Double unitPrice;
    private Double totalPrice;
    private String message;
}
