package com.linktic.productos.utilities;

public final class AppConstants {

    private AppConstants() {}

    // Códigos de error
    public static final String ERROR_MESSAGE_PRODUCT_NOT_FOUND = "El producto no existe en el sistema";

    // Otros valores constantes
    public static final String URL_MICROSERVICES_PRODUCT = "http://productos-service:8080/product";
    public static final String PRIVATE_KEY = "LINKTIC_SECRET_123";
    public static final String SUCCESS_PURCHASE = "Compra realizada exitosamente";
}