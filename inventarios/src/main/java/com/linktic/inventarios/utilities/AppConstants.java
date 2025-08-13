package com.linktic.inventarios.utilities;

public final class AppConstants {

    private AppConstants() {}

    // Códigos de error
    public static final String ERROR_MESSAGE_PRODUCT_NOT_FOUND = "Producto no encontrado en microservicio productos";
    public static final String ERROR_MESSAGE_INVENTARY_NOT_FOUND = "No hay inventario para este producto";
    public static final String INSUFFICIENT_INVENTARY = "Inventario insuficiente";

    // Otros valores constantes
    public static final String URL_MICROSERVICES_PRODUCT = "http://productos-service:8080/product";
    public static final String PRIVATE_KEY = "LINKTIC_SECRET_123";
    public static final String SUCCESS_PURCHASE = "Compra realizada exitosamente";
}