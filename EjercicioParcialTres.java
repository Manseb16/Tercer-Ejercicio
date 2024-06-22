/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ejercicioparcialtres;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class Categoria {
    String nombre;

    public Categoria(String nombre) {
        this.nombre = nombre;
    }
}

class Producto {
    String nombre;
    double precio;
    Categoria categoria;

    public Producto(String nombre, double precio, Categoria categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    public String mostrarInfo() {
        return nombre + " - $" + precio;
    }
}

class Cliente {
    String nombre;
    String apellido;
    int idCliente;
    List<Orden> ordenes;

    public Cliente(String nombre, String apellido, int idCliente) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.idCliente = idCliente;
        this.ordenes = new ArrayList<>();
    }

    public String mostrarInfo() {
        return nombre + " " + apellido + " (ID: " + idCliente + ")";
    }
}

class ItemOrden {
    Producto producto;
    int cantidad;

    public ItemOrden(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }
}

class Orden {
    Cliente cliente;
    List<ItemOrden> items;
    double total;

    public Orden(Cliente cliente) {
        this.cliente = cliente;
        this.items = new ArrayList<>();
        this.total = 0;
    }

    public void agregarItem(ItemOrden item) {
        items.add(item);
    }

    public void calcularTotal() {
        total = items.stream().mapToDouble(item -> item.producto.precio * item.cantidad).sum();
    }
}

class Tienda {
    List<Producto> productos;
    List<Cliente> clientes;
    List<Orden> ordenes;
    List<Categoria> categorias;

    public Tienda() {
        productos = new ArrayList<>();
        clientes = new ArrayList<>();
        ordenes = new ArrayList<>();
        categorias = new ArrayList<>();

        Categoria categoriaElectronicos = new Categoria("Electrónicos");
        Categoria categoriaCuidadoPiel = new Categoria("Cuidado para la piel");
        categorias.add(categoriaElectronicos);
        categorias.add(categoriaCuidadoPiel);

        registrarProducto(new Producto("Tablet", 900000, categoriaElectronicos));
        registrarProducto(new Producto("Celular", 1000000, categoriaElectronicos));
        registrarProducto(new Producto("Computador", 2000000, categoriaElectronicos));
        registrarProducto(new Producto("Shampoo", 25000, categoriaCuidadoPiel));
        registrarProducto(new Producto("Acondicionador", 20000, categoriaCuidadoPiel));
    }

    public void registrarProducto(Producto producto) {
        productos.add(producto);
    }

    public void registrarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public Orden crearOrden(Cliente cliente, List<ItemOrden> items) {
        Orden orden = new Orden(cliente);
        for (ItemOrden item : items) {
            orden.agregarItem(item);
        }
        orden.calcularTotal();
        ordenes.add(orden);
        cliente.ordenes.add(orden);
        return orden;
    }
}

public class EjercicioParcialTres extends JFrame {
    private Tienda tienda;
    private JTextField nombreClienteField;
    private JComboBox<String> productosCombo;
    private JTextField cantidadField;
    private DefaultListModel<String> productosListModel;
    private DefaultListModel<String> clientesListModel;
    private JList<String> productosJList;
    private JList<String> clientesJList;

    public EjercicioParcialTres() {
        tienda = new Tienda();
        setTitle("Gestión de Tienda");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().setBackground(Color.BLACK);

        JLabel nombreClienteLabel = new JLabel("Nombre del Cliente:");
        nombreClienteLabel.setForeground(Color.WHITE);
        add(nombreClienteLabel);

        nombreClienteField = new JTextField(20);
        add(nombreClienteField);

        JLabel productoLabel = new JLabel("Producto:");
        productoLabel.setForeground(Color.WHITE);
        add(productoLabel);

        productosCombo = new JComboBox<>();
        for (Producto producto : tienda.productos) {
            productosCombo.addItem(producto.nombre);
        }
        add(productosCombo);

        JLabel cantidadLabel = new JLabel("Cantidad:");
        cantidadLabel.setForeground(Color.WHITE);
        add(cantidadLabel);

        cantidadField = new JTextField(20);
        add(cantidadField);

        JButton registrarProductoButton = new JButton("Registrar Producto");
        registrarProductoButton.addActionListener(e -> registrarProducto());
        add(registrarProductoButton);

        JButton registrarClienteButton = new JButton("Registrar Cliente");
        registrarClienteButton.addActionListener(e -> registrarCliente());
        add(registrarClienteButton);

        JButton crearOrdenButton = new JButton("Crear Orden");
        crearOrdenButton.addActionListener(e -> crearOrden());
        add(crearOrdenButton);

        JLabel productosRegistradosLabel = new JLabel("Productos Registrados:");
        productosRegistradosLabel.setForeground(Color.WHITE);
        add(productosRegistradosLabel);

        productosListModel = new DefaultListModel<>();
        productosJList = new JList<>(productosListModel);
        add(new JScrollPane(productosJList));

        JLabel clientesRegistradosLabel = new JLabel("Clientes Registrados:");
        clientesRegistradosLabel.setForeground(Color.WHITE);
        add(clientesRegistradosLabel);

        clientesListModel = new DefaultListModel<>();
        clientesJList = new JList<>(clientesListModel);
        add(new JScrollPane(clientesJList));

        cargarProductos();
        cargarClientes();
    }

    private void registrarProducto() {
        String nombreProducto = (String) productosCombo.getSelectedItem();
        double precio = 0;
        Categoria categoria = null;

        for (Producto prod : tienda.productos) {
            if (prod.nombre.equals(nombreProducto)) {
                precio = prod.precio;
                categoria = prod.categoria;
                break;
            }
        }

        Producto producto = new Producto(nombreProducto, precio, categoria);
        tienda.registrarProducto(producto);

        productosListModel.addElement(producto.mostrarInfo());
        cargarProductos();
    }

    private void registrarCliente() {
        String nombreCliente = nombreClienteField.getText();
        String apellido = "Pulido";
        int idCliente = tienda.clientes.size() + 1;
        Cliente cliente = new Cliente(nombreCliente, apellido, idCliente);
        tienda.registrarCliente(cliente);

        clientesListModel.addElement(cliente.mostrarInfo());
        cargarClientes();
    }

    private void cargarProductos() {
        productosCombo.removeAllItems();
        for (Producto producto : tienda.productos) {
            productosCombo.addItem(producto.nombre);
        }
    }

    private void cargarClientes() {
        clientesListModel.clear();
        for (Cliente cliente : tienda.clientes) {
            clientesListModel.addElement(cliente.mostrarInfo());
        }
    }

    private void crearOrden() {
        String nombreCliente = nombreClienteField.getText();

        if (nombreCliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre del cliente.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente clienteEncontrado = null;
        for (Cliente cliente : tienda.clientes) {
            if (cliente.nombre.equals(nombreCliente)) {
                clienteEncontrado = cliente;
                break;
            }
        }

        if (clienteEncontrado == null) {
            JOptionPane.showMessageDialog(this, "No se encontró al cliente '" + nombreCliente + "'.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombreProducto = (String) productosCombo.getSelectedItem();
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Producto productoEncontrado = null;
        for (Producto producto : tienda.productos) {
            if (producto.nombre.equals(nombreProducto)) {
                productoEncontrado = producto;
                break;
            }
        }

        if (productoEncontrado == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el producto '" + nombreProducto + "'.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ItemOrden item = new ItemOrden(productoEncontrado, cantidad);
        List<ItemOrden> items = new ArrayList<>();
        items.add(item);
        Orden orden = tienda.crearOrden(clienteEncontrado, items);

        String detallesOrden = "Cliente: " + clienteEncontrado.mostrarInfo() + "\n" +
                               "Producto: " + productoEncontrado.nombre + "\n" +
                               "Categoría: " + productoEncontrado.categoria.nombre + "\n" +
                               "Cantidad: " + cantidad + "\n" +
                               "Total: $" + orden.total;

        JOptionPane.showMessageDialog(this, detallesOrden, "Orden Creada", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EjercicioParcialTres frame = new EjercicioParcialTres();
            frame.setVisible(true);
        });
    }
}