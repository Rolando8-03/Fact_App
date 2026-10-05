package ni.edu.uam.fact_app.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ni.edu.uam.fact_app.dao.ProductoDAO;
import ni.edu.uam.fact_app.dao.VentaDAO;
import ni.edu.uam.fact_app.model.Venta;
import ni.edu.uam.fact_app.util.Alertas;
import ni.edu.uam.fact_app.model.DetalleVenta;
import ni.edu.uam.fact_app.model.Producto;
import ni.edu.uam.fact_app.model.Usuario;
import ni.edu.uam.fact_app.util.SesionUsuario;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

public class VentaController {

    @FXML private Label lblNumeroFactura;
    @FXML private Label lblVendedor;
    @FXML private TextField txtBuscarProducto;
    @FXML private ComboBox<Producto> cmbProducto;
    @FXML private TextField txtCantidad;
    @FXML private Label lblStockDisponible;

    @FXML private TableView<DetalleVenta> tblDetalle;
    @FXML private TableColumn<DetalleVenta, String> colCodigo;
    @FXML private TableColumn<DetalleVenta, String> colProducto;
    @FXML private TableColumn<DetalleVenta, Integer> colCantidad;
    @FXML private TableColumn<DetalleVenta, BigDecimal> colPrecio;
    @FXML private TableColumn<DetalleVenta, BigDecimal> colSubtotal;

    @FXML private Label lblSubtotal;
    @FXML private Label lblIva;
    @FXML private Label lblTotal;

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ObservableList<DetalleVenta> carrito = FXCollections.observableArrayList();
    private final ObservableList<Producto> listaDisponibles = FXCollections.observableArrayList();
    private FilteredList<Producto> filtroDisponibles;


    @FXML
    private void initialize() {
        lblNumeroFactura.setText("Nueva factura");
        Usuario actual = SesionUsuario.getUsuarioActual();
        if (actual != null && actual.getEmpleado() != null) {
            lblVendedor.setText(actual.getEmpleado().getNombres() + " " + actual.getEmpleado().getApellidos());
        }

        recargarComboProductos();

        filtroDisponibles = new FilteredList<>(listaDisponibles, p -> true);
        cmbProducto.setItems(filtroDisponibles);

        if (txtBuscarProducto != null) {
            txtBuscarProducto.textProperty().addListener((obs, oldV, texto) -> {
                filtroDisponibles.setPredicate(p -> {
                    if (texto == null || texto.isBlank()) return true;
                    String b = texto.toLowerCase();
                    return p.getNombre().toLowerCase().contains(b) || p.getCodigo().toLowerCase().contains(b);
                });
                if (!filtroDisponibles.isEmpty()) {
                    cmbProducto.setValue(filtroDisponibles.get(0));
                }
            });
        }

        cmbProducto.getSelectionModel().selectedItemProperty().addListener((obs, oldV, prod) -> {
            if (prod != null) {
                lblStockDisponible.setText("Disponibles: " + prod.getExistencia() + " unids.");
            } else {
                lblStockDisponible.setText("Disponibles: -");
            }
        });

        colCodigo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getCodigoProducto()));
        colProducto.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombreProducto()));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        tblDetalle.setItems(carrito);
        tblDetalle.setPlaceholder(new Label("No hay productos agregados a la factura."));
        calcularTotales();
        txtCantidad.sceneProperty().addListener((o, anterior, escena) -> {
            if (escena != null) escena.windowProperty().addListener((w, antes, ventana) -> {
                if (ventana != null) ventana.setOnCloseRequest(event -> {
                    if (!carrito.isEmpty() && !Alertas.confirmar("¿Desea descartar la venta sin guardar?")) event.consume();
                });
            });
        });
    }

    @FXML
    private void recargarComboProductos() {
        try {
            java.util.List<Producto> nuevos = productoDAO.listar();
            listaDisponibles.clear();
            for (Producto p : nuevos) {
                if (p.isActivo() && p.getExistencia() > 0) {
                    listaDisponibles.add(p);
                }
            }
        } catch (SQLException e) {
            mensaje(Alert.AlertType.ERROR, "Error al cargar productos desde PostgreSQL: " + e.getMessage());
        }
    }

    @FXML
    private void agregarAlCarrito() {
        Producto prod = cmbProducto.getValue();
        if (prod == null) {
            mensaje(Alert.AlertType.WARNING, "Seleccione un producto del catálogo.");
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) {
                mensaje(Alert.AlertType.WARNING, "La cantidad a facturar debe ser mayor a 0.");
                return;
            }
        } catch (NumberFormatException e) {
            mensaje(Alert.AlertType.ERROR, "La cantidad debe ser un número entero válido.");
            return;
        }

        int yaEnCarrito = 0;
        for (DetalleVenta d : carrito) {
            if (d.getProducto().getCodigo().equals(prod.getCodigo())) {
                yaEnCarrito += d.getCantidad();
            }
        }

        if (((long) yaEnCarrito + cantidad) > prod.getExistencia()) {
            mensaje(Alert.AlertType.ERROR, "Stock insuficiente. En stock: " + prod.getExistencia()
                    + " (Ya tienes " + yaEnCarrito + " en la orden)");
            return;
        }

        boolean existe = false;
        for (DetalleVenta d : carrito) {
            if (d.getProducto().getCodigo().equals(prod.getCodigo())) {
                d.setCantidad(d.getCantidad() + cantidad);
                d.setSubtotal(d.getPrecioUnitario().multiply(new BigDecimal(d.getCantidad())));
                existe = true;
                break;
            }
        }

        if (!existe) {
            carrito.add(new DetalleVenta(prod, cantidad));
        }

        tblDetalle.refresh();
        calcularTotales();
        txtCantidad.setText("1");
        if (txtBuscarProducto != null) txtBuscarProducto.clear();
        cmbProducto.getSelectionModel().clearSelection();
    }

    @FXML
    private void removerDelCarrito() {
        DetalleVenta sel = tblDetalle.getSelectionModel().getSelectedItem();
        if (sel != null) {
            carrito.remove(sel);
            calcularTotales();
        } else {
            mensaje(Alert.AlertType.WARNING, "Seleccione un artículo para remover.");
        }
    }

    private void calcularTotales() {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (DetalleVenta d : carrito) {
            subtotal = subtotal.add(d.getSubtotal());
        }
        BigDecimal iva = subtotal.multiply(new BigDecimal("0.15")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(iva).setScale(2, RoundingMode.HALF_UP);

        lblSubtotal.setText("C$ " + subtotal.setScale(2, RoundingMode.HALF_UP));
        lblIva.setText("C$ " + iva);
        lblTotal.setText("C$ " + total);
    }


    @FXML
    private void finalizarVenta() {
        if (carrito.isEmpty()) {
            mensaje(Alert.AlertType.WARNING, "No hay productos agregados a la factura."); return;
        }
        if (!Alertas.confirmar("¿Desea guardar la factura y descontar las existencias?")) return;
        try {
            Venta venta = new VentaDAO().guardar(carrito, SesionUsuario.getUsuarioActual());
            carrito.clear();
            lblNumeroFactura.setText(venta.getNumeroFactura());
            mensaje(Alert.AlertType.INFORMATION, "Factura " + venta.getNumeroFactura() + " guardada correctamente.\nTotal: C$ " + venta.getTotal());
            ((Stage) txtCantidad.getScene().getWindow()).close();
        } catch (SQLException e) { Alertas.errorBD(e); }
    }

    @FXML
    private void cancelar() {
        if (carrito.isEmpty() || Alertas.confirmar("¿Desea descartar la venta sin guardar?"))
            ((Stage) txtCantidad.getScene().getWindow()).close();
    }

    private void mensaje(Alert.AlertType tipo, String texto) {
        Alertas.mostrar(tipo, texto);
    }
}
