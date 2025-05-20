package com.techlab.main;

import java.util.*;
import com.techlab.productos.Productos;
import com.techlab.pedidos.pedidos;
import com.techlab.excepciones.SinStockException;

/**
 * Clase principal que gestiona la interacción con el usuario y el flujo del sistema.
 */
public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final List<Productos> productos = new ArrayList<>();
    private static final List<pedidos> pedidos = new ArrayList<>();

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerOpcion();
            if (opcion != -1) {
                ejecutarOpcion(opcion);
            }
        } while (opcion != 7);
    }

    private static void mostrarMenu() {
        System.out.println("=== SISTEMA DE GESTION ===");
        System.out.println("1) Agregar producto");
        System.out.println("2) Listar productos");
        System.out.println("3) Buscar/Actualizar producto");
        System.out.println("4) Eliminar producto");
        System.out.println("5) Crear un pedido");
        System.out.println("6) Listar pedidos");
        System.out.println("7) Salir");
        System.out.print("Elija una opcion: ");
    }

    private static int leerOpcion() {
        String line = sc.nextLine().trim();
        try {
            int opt = Integer.parseInt(line);
            if (opt >= 1 && opt <= 7) return opt;
        } catch (NumberFormatException e) {
            // Ignorar excepción y mostrar mensaje de error
        }
        System.out.println("Opcion invalida, intente nuevamente.");
        return -1; // Retornar -1 para indicar una opción inválida
    }

    private static void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> altaProducto();
            case 2 -> listaProductos();
            case 3 -> actualizarProducto();
            case 4 -> eliminarProducto();
            case 5 -> crearPedido();
            case 6 -> listarPedidos();
            case 7 -> System.out.println("Sistema cerrado.");
            default -> System.out.println("Opción no válida.");
        }
    }

    private static void altaProducto() {
        try {
            System.out.print("Nombre: ");
            String nombre = leerLineaNoVacia("Nombre");
            double precio = Double.parseDouble(leerLineaNoVacia("Precio"));
            int stock = Integer.parseInt(leerLineaNoVacia("Stock"));
            Productos nuevo = new Productos(nombre, precio, stock);
            productos.add(nuevo);
            System.out.println("Producto agregado exitosamente.");
        } catch (NumberFormatException ex) {
            System.out.println("Error: debe ingresar un número válido.");
        }
    }

    private static void listaProductos() {
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
        } else {
            productos.forEach(System.out::println);
        }
    }

    private static void actualizarProducto() {
        try {
            System.out.print("ID producto a actualizar: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            Productos p = buscarProductoPorId(id);
            if (p == null) {
                System.out.println("Producto no encontrado.");
                return;
            }
            System.out.println("Actualizando: " + p);
            Double nuevoPrecio = leerPrecio();
            Integer nuevoStock = leerStock();
            if (nuevoPrecio != null) {
                p.setPrecio(nuevoPrecio);
            }
            if (nuevoStock != null) {
                p.setStock(nuevoStock);
            }
            System.out.println("Producto actualizado: " + p);
        } catch (NumberFormatException ex) {
            System.out.println("Error: entrada inválida.");
        } catch (SinStockException ex) {
            System.out.println("Error al actualizar producto: " + ex.getMessage());
        }
    }

    private static void eliminarProducto() {
        try {
            System.out.print("ID producto a eliminar: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            Productos p = buscarProductoPorId(id);
            if (p != null) {
                productos.remove(p);
                System.out.println("Producto eliminado.");
            } else {
                System.out.println("Producto no encontrado.");
            }
        } catch (NumberFormatException ex) {
            System.out.println("Error: ID inválido.");
        }
    }

    private static void crearPedido() {
        Map<Integer, Integer> mapa = new HashMap<>();
        try {
            System.out.print("¿Cuántos productos desea agregar al pedido? ");
            int n = Integer.parseInt(sc.nextLine().trim());
            for (int i = 1; i <= n; i++) {
                System.out.print("ID producto " + i + ": ");
                int id = Integer.parseInt(sc.nextLine().trim());
                System.out.print("Cantidad: ");
                int cant = Integer.parseInt(sc.nextLine().trim());
                Productos prod = buscarProductoPorId(id);
                if (prod == null) {
                    System.out.println("Producto no encontrado con ID: " + id);
                    continue;
                }
                if (prod.getStock() >= cant) {
                    mapa.put(id, cant);
                    prod.setStock(prod.getStock() - cant); // Disminuir stock
                } else {
                    throw new SinStockException("Stock insuficiente para el producto ID: " + id);
                }
            }
            Pedido pedido = new Pedido(mapa);
            pedidos.add(pedido);
            System.out.println("Pedido creado:");
            System.out.println(pedido);
        } catch (NumberFormatException ex) {
            System.out.println("Error: entrada numérica inválida.");
        } catch (SinStockException ex) {
            System.out.println("Error al crear pedido: " + ex.getMessage());
        }
    }

    private static void listarPedidos() {
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
        } else {
            pedidos.forEach(System.out::println);
        }
    }

    private static String leerLineaNoVacia(String campo) {
        String line;
        do {
            line = sc.nextLine().trim();
            if (line.isEmpty()) {
                System.out.print(campo + " no puede estar vacío. Intente de nuevo: ");
            }
        } while (line.isEmpty());
        return line;
    }

    private static Double leerPrecio() {
        System.out.print("Nuevo precio (ENTER para omitir): ");
        String precioStr = sc.nextLine().trim();
        if (precioStr.isEmpty()) return null;
        try {
            return Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            System.out.println("Entrada de precio inválida. Se omitirá el cambio.");
            return null;
        }
    }

    private static Integer leerStock() {
        System.out.print("Nuevo stock (ENTER para omitir): ");
        String stockStr = sc.nextLine().trim();
        if (stockStr.isEmpty()) return null;
        try {
            return Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            System.out.println("Entrada de stock inválida. Se omitirá el cambio.");
            return null;
        }
    }

    private static Productos buscarProductoPorId(int id) {
        for (Productos p : productos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null; // Producto no encontrado
    }
}

