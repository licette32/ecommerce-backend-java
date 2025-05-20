package com.techlab.productos;

import com.techlab.excepciones.SinStockException;

public class Productos {
    private static int contador = 0; // Contador para asignar ID único
    private final int id;
    private String nombre;
    private double precio;
    private int stock;

    public Productos(String nombre, double precio, int stock) {
        this.id = ++contador; // Asignar ID único
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) throws SinStockException {
        if (stock < 0) {
            throw new SinStockException("El stock no puede ser negativo.");
        }
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Nombre: " + nombre + " | Precio: $" + precio + " | Stock: " + stock;
    }
}
