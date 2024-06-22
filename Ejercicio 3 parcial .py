import tkinter as tk
from tkinter import ttk, messagebox

class Categoria:
    def __init__(self, nombre):
        self.nombre = nombre

class Producto:
    def __init__(self, nombre, precio, categoria):
        self.nombre = nombre
        self.precio = precio
        self.categoria = categoria  

    def mostrar_info(self):
        return f"{self.nombre} - ${self.precio}"

class Cliente:
    def __init__(self, nombre, apellido, id_cliente):
        self.nombre = nombre
        self.apellido = apellido
        self.id_cliente = id_cliente
        self.ordenes = []  

    def mostrar_info(self):
        return f"{self.nombre} {self.apellido} (ID: {self.id_cliente})"

class ItemOrden:
    def __init__(self, producto, cantidad):
        self.producto = producto  
        self.cantidad = cantidad

class Orden:
    def __init__(self, cliente):
        self.cliente = cliente  
        self.items = []  
        self.total = 0

    def agregar_item(self, item):
        self.items.append(item)

    def calcular_total(self):
        self.total = sum(item.producto.precio * item.cantidad for item in self.items)

class Tienda:
    def __init__(self):
        self.productos = []   
        self.clientes = []    
        self.ordenes = []     
        self.categorias = []  

        
        categoria_electronicos = Categoria("Electrónicos")
        categoria_cuidado_piel = Categoria("Cuidado para la piel")
        self.categorias.extend([categoria_electronicos, categoria_cuidado_piel])

      
        self.registrar_producto(Producto("Tablet", 900000, categoria_electronicos))
        self.registrar_producto(Producto("Celular", 1000000, categoria_electronicos))
        self.registrar_producto(Producto("Computador", 2000000, categoria_electronicos))
        self.registrar_producto(Producto("Shampoo", 25000, categoria_cuidado_piel))
        self.registrar_producto(Producto("Acondicionador", 20000, categoria_cuidado_piel))

    def registrar_producto(self, producto):
        self.productos.append(producto)

    def registrar_cliente(self, cliente):
        self.clientes.append(cliente)

    def crear_orden(self, cliente, items):
        orden = Orden(cliente)
        for item in items:
            orden.agregar_item(item)
        orden.calcular_total()
        self.ordenes.append(orden)
        cliente.ordenes.append(orden)  
        return orden


class TiendaGUI:
    def __init__(self, root):
        self.root = root
        self.root.title("Gestión de Tienda")
        self.root.configure(bg='black')  
        self.tienda = Tienda()

       
        self.nombre_cliente_var = tk.StringVar()
        self.producto_var = tk.StringVar()
        self.cantidad_var = tk.IntVar()

       
        self.label_nombre_cliente = tk.Label(root, text="Nombre del Cliente:", bg='black', fg='white')
        self.label_nombre_cliente.pack()

        self.entry_nombre_cliente = tk.Entry(root, textvariable=self.nombre_cliente_var, width=50)
        self.entry_nombre_cliente.pack()

        self.label_producto = tk.Label(root, text="Producto:", bg='black', fg='white')
        self.label_producto.pack()

        self.combo_productos = ttk.Combobox(root, textvariable=self.producto_var, state="readonly", width=47)
        self.combo_productos.pack()

        self.label_cantidad = tk.Label(root, text="Cantidad:", bg='black', fg='white')
        self.label_cantidad.pack()

        self.entry_cantidad = tk.Entry(root, textvariable=self.cantidad_var, width=50)
        self.entry_cantidad.pack()

        self.boton_registrar_producto = tk.Button(root, text="Registrar Producto", command=self.registrar_producto)
        self.boton_registrar_producto.pack()

        self.boton_registrar_cliente = tk.Button(root, text="Registrar Cliente", command=self.registrar_cliente)
        self.boton_registrar_cliente.pack()

        self.boton_crear_orden = tk.Button(root, text="Crear Orden", command=self.crear_orden)
        self.boton_crear_orden.pack()

        self.label_productos_registrados = tk.Label(root, text="Productos Registrados:", bg='black', fg='white')
        self.label_productos_registrados.pack()

        self.lista_productos = tk.Listbox(root, height=5, width=50)
        self.lista_productos.pack()

        self.label_clientes_registrados = tk.Label(root, text="Clientes Registrados:", bg='black', fg='white')
        self.label_clientes_registrados.pack()

        self.lista_clientes = tk.Listbox(root, height=5, width=50)
        self.lista_clientes.pack()

        self.cargar_productos()
        self.cargar_clientes()

    def registrar_producto(self):
        nombre_producto = self.producto_var.get()
        precio = 0  
        categoria = None

        
        for prod in self.tienda.productos:
            if prod.nombre == nombre_producto:
                precio = prod.precio
                categoria = prod.categoria
                break

        producto = Producto(nombre_producto, precio, categoria)
        self.tienda.registrar_producto(producto)

        self.lista_productos.insert(tk.END, producto.mostrar_info())
        self.cargar_productos()

    def registrar_cliente(self):
        nombre_cliente = self.nombre_cliente_var.get()
        apellido = "Pulido"  
        id_cliente = len(self.tienda.clientes) + 1  
        cliente = Cliente(nombre_cliente, apellido, id_cliente)
        self.tienda.registrar_cliente(cliente)

        self.lista_clientes.insert(tk.END, cliente.mostrar_info())
        self.cargar_clientes()

    def cargar_productos(self):
        self.combo_productos['values'] = [producto.nombre for producto in self.tienda.productos]

    def cargar_clientes(self):
        self.lista_clientes.delete(0, tk.END)  # Limpiar la lista de clientes
        for cliente in self.tienda.clientes:
            self.lista_clientes.insert(tk.END, cliente.mostrar_info())

    def crear_orden(self):
        nombre_cliente = self.nombre_cliente_var.get()

        if not nombre_cliente:
            messagebox.showwarning("Error", "Ingrese el nombre del cliente.")
            return

        cliente_encontrado = None
        for cliente in self.tienda.clientes:
            if cliente.nombre == nombre_cliente:
                cliente_encontrado = cliente
                break

        if not cliente_encontrado:
            messagebox.showwarning("Error", f"No se encontró al cliente '{nombre_cliente}'.")
            return

        nombre_producto = self.producto_var.get()
        cantidad = self.cantidad_var.get()

        if not nombre_producto:
            messagebox.showwarning("Error", "Seleccione un producto.")
            return

        producto_encontrado = None
        for producto in self.tienda.productos:
            if producto.nombre == nombre_producto:
                producto_encontrado = producto
                break

        if not producto_encontrado:
            messagebox.showwarning("Error", f"No se encontró el producto '{nombre_producto}'.")
            return

        item = ItemOrden(producto_encontrado, cantidad)
        orden = self.tienda.crear_orden(cliente_encontrado, [item])

       
        detalles_orden = f"Cliente: {cliente_encontrado.mostrar_info()}\n"
        detalles_orden += f"Producto: {producto_encontrado.nombre}\n"
        detalles_orden += f"Categoría: {producto_encontrado.categoria.nombre}\n"
        detalles_orden += f"Cantidad: {cantidad}\n"
        detalles_orden += f"Total: ${orden.total}"

        messagebox.showinfo("Orden Creada", detalles_orden)

if __name__ == "__main__":
    root = tk.Tk()
    root.configure(bg='black')  # Fondo negro
    app = TiendaGUI(root)
    root.mainloop()
