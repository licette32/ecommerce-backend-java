package com.techlab.excepciones;

/**
 * Excepción que indica que no hay suficiente stock para completar una operación.
 */
public class SinStockException extends Exception {

    /**
     * Constructor que recibe el mensaje de error.
     * 
     * @param mensaje Mensaje que describe la excepción.
     */
    public SinStockException(String mensaje) {
        super(mensaje);
    }
}

