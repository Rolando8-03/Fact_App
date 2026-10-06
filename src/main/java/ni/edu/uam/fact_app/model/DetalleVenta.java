package ni.edu.uam.fact_app.model;

import java.math.BigDecimal;

public class DetalleVenta {
    private Producto producto;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public DetalleVenta(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecioVenta();
        this.subtotal = this.precioUnitario.multiply(new BigDecimal(cantidad));
    }

    public String getNombreProducto() {
        return producto != null ? producto.getNombre() : "";
    }

    public String getCodigoProducto() {
        return producto != null ? producto.getCodigo() : "";
    }

    public Producto getProducto() {
        return this.producto;
    }

    public int getCantidad() {
        return this.cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return this.precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return this.subtotal;
    }

    public void setProducto(final Producto producto) {
        this.producto = producto;
    }

    public void setCantidad(final int cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecioUnitario(final BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public void setSubtotal(final BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof DetalleVenta)) return false;
        final DetalleVenta other = (DetalleVenta) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        if (this.getCantidad() != other.getCantidad()) return false;
        final java.lang.Object this$producto = this.getProducto();
        final java.lang.Object other$producto = other.getProducto();
        if (this$producto == null ? other$producto != null : !this$producto.equals(other$producto)) return false;
        final java.lang.Object this$precioUnitario = this.getPrecioUnitario();
        final java.lang.Object other$precioUnitario = other.getPrecioUnitario();
        if (this$precioUnitario == null ? other$precioUnitario != null : !this$precioUnitario.equals(other$precioUnitario)) return false;
        final java.lang.Object this$subtotal = this.getSubtotal();
        final java.lang.Object other$subtotal = other.getSubtotal();
        if (this$subtotal == null ? other$subtotal != null : !this$subtotal.equals(other$subtotal)) return false;
        return true;
    }

    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof DetalleVenta;
    }

    @java.lang.Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getCantidad();
        final java.lang.Object $producto = this.getProducto();
        result = result * PRIME + ($producto == null ? 43 : $producto.hashCode());
        final java.lang.Object $precioUnitario = this.getPrecioUnitario();
        result = result * PRIME + ($precioUnitario == null ? 43 : $precioUnitario.hashCode());
        final java.lang.Object $subtotal = this.getSubtotal();
        result = result * PRIME + ($subtotal == null ? 43 : $subtotal.hashCode());
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "DetalleVenta(producto=" + this.getProducto() + ", cantidad=" + this.getCantidad() + ", precioUnitario=" + this.getPrecioUnitario() + ", subtotal=" + this.getSubtotal() + ")";
    }

    public DetalleVenta() {
    }

    public DetalleVenta(final Producto producto, final int cantidad, final BigDecimal precioUnitario, final BigDecimal subtotal) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }
}
