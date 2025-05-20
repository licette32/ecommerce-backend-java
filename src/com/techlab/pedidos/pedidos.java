package com.techlab.pedidos;

import com.techlab.productos.Productos;
import com.techlab.excepciones.SinStockException;

import java.util.*;

/**
 * Clase que representa un pedido con líneas de productos y cantidades.
 */
public class pedidos {
    private static int contador = 0;
    private final int id;
    private final Map<Productos, Integer> productos; // Producto y cantidad

    public pedidos(Map<Integer, Integer> mapaCantidades, List<Productos> catalogoProductos) throws SinStockException {
        this.id = ++contador;
        this.productos = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> entry : mapaCantidades.entrySet()) {
            int prodId = entry.getKey();
            int cantidad = entry.getValue();

            // Buscar producto en catálogo
            Productos producto = catalogoProductos.stream()
                    .filter(p -> p.getId() == prodId)
                    .findFirst()
                    .orElseThrow(() -> new SinStockException("Producto no encontrado con ID: " + prodId));

            // Validar stock
            if (producto.getStock() < cantidad) {
                throw new SinStockException("Stock insuficiente para producto ID: " + prodId);
            }

            // Actualizar stock del producto
            producto.setStock(producto.getStock() - cantidad);

            // Agregar producto y cantidad a pedido
            productos.put(producto, cantidad);
        }
    }

    public int getId() {
        return id;
    }

    public Map<Productos, Integer> getProductos() {
        return Collections.unmodifiableMap(productos);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Pedido #").append(id).append("\n");
        productos.forEach((producto, cantidad) -> {
            sb.append("Producto: ").append(producto.getNombre())
              .append(" (ID: ").append(producto.getId()).append("), ")
              .append("Cantidad: ").append(cantidad).append(", ")
              .append("Precio Unitario: $").append(producto.getPrecio()).append(", ")
              .append("Subtotal: $").append(producto.getPrecio() * cantidad).append("\n");
        });
        double total = productos.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrecio() * e.getValue())
                .sum();
        sb.append(String.format("Total del pedido: $%.2f", total));
        return sb.toString();
    }
}
