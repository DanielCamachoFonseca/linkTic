package com.linktic.inventarios.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RequestException extends RuntimeException {

    private String code;

    /**
     * Obtain request exception
     *
     * @param code
     * @param message
     */
    public RequestException(String code, String message) {
        super(message);
        this.code = code;
    }
}
