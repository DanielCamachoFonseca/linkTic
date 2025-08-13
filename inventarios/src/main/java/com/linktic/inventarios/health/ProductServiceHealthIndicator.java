package com.linktic.inventarios.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        try {
            return Health.up().withDetail("product-service", "Disponible").build();
        } catch (Exception e) {
            return Health.down().withDetail("product-service", "No disponible").build();
        }
    }
}