package ni.edu.uam.fact_app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venta {
    private Integer id;
    private String numeroFactura;
    private LocalDateTime fecha;
    private Empleado vendedor;
    private List<DetalleVenta> detalles = new ArrayList<>();
    private BigDecimal subtotal;
    private BigDecimal iva;
    private BigDecimal total;

    public Integer getId() {
        return this.id;
    }

    public String getNumeroFactura() {
        return this.numeroFactura;
    }

    public LocalDateTime getFecha() {
        return this.fecha;
    }

    public Empleado getVendedor() {
        return this.vendedor;
    }

    public List<DetalleVenta> getDetalles() {
        return this.detalles;
    }

    public BigDecimal getSubtotal() {
        return this.subtotal;
    }

    public BigDecimal getIva() {
        return this.iva;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setNumeroFactura(final String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public void setFecha(final LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setVendedor(final Empleado vendedor) {
        this.vendedor = vendedor;
    }

    public void setDetalles(final List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    public void setSubtotal(final BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public void setIva(final BigDecimal iva) {
        this.iva = iva;
    }

    public void setTotal(final BigDecimal total) {
        this.total = total;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof Venta)) return false;
        final Venta other = (Venta) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        final java.lang.Object this$id = this.getId();
        final java.lang.Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final java.lang.Object this$numeroFactura = this.getNumeroFactura();
        final java.lang.Object other$numeroFactura = other.getNumeroFactura();
        if (this$numeroFactura == null ? other$numeroFactura != null : !this$numeroFactura.equals(other$numeroFactura)) return false;
        final java.lang.Object this$fecha = this.getFecha();
        final java.lang.Object other$fecha = other.getFecha();
        if (this$fecha == null ? other$fecha != null : !this$fecha.equals(other$fecha)) return false;
        final java.lang.Object this$vendedor = this.getVendedor();
        final java.lang.Object other$vendedor = other.getVendedor();
        if (this$vendedor == null ? other$vendedor != null : !this$vendedor.equals(other$vendedor)) return false;
        final java.lang.Object this$detalles = this.getDetalles();
        final java.lang.Object other$detalles = other.getDetalles();
        if (this$detalles == null ? other$detalles != null : !this$detalles.equals(other$detalles)) return false;
        final java.lang.Object this$subtotal = this.getSubtotal();
        final java.lang.Object other$subtotal = other.getSubtotal();
        if (this$subtotal == null ? other$subtotal != null : !this$subtotal.equals(other$subtotal)) return false;
        final java.lang.Object this$iva = this.getIva();
        final java.lang.Object other$iva = other.getIva();
        if (this$iva == null ? other$iva != null : !this$iva.equals(other$iva)) return false;
        final java.lang.Object this$total = this.getTotal();
        final java.lang.Object other$total = other.getTotal();
        if (this$total == null ? other$total != null : !this$total.equals(other$total)) return false;
        return true;
    }

    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof Venta;
    }

    @java.lang.Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final java.lang.Object $numeroFactura = this.getNumeroFactura();
        result = result * PRIME + ($numeroFactura == null ? 43 : $numeroFactura.hashCode());
        final java.lang.Object $fecha = this.getFecha();
        result = result * PRIME + ($fecha == null ? 43 : $fecha.hashCode());
        final java.lang.Object $vendedor = this.getVendedor();
        result = result * PRIME + ($vendedor == null ? 43 : $vendedor.hashCode());
        final java.lang.Object $detalles = this.getDetalles();
        result = result * PRIME + ($detalles == null ? 43 : $detalles.hashCode());
        final java.lang.Object $subtotal = this.getSubtotal();
        result = result * PRIME + ($subtotal == null ? 43 : $subtotal.hashCode());
        final java.lang.Object $iva = this.getIva();
        result = result * PRIME + ($iva == null ? 43 : $iva.hashCode());
        final java.lang.Object $total = this.getTotal();
        result = result * PRIME + ($total == null ? 43 : $total.hashCode());
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Venta(id=" + this.getId() + ", numeroFactura=" + this.getNumeroFactura() + ", fecha=" + this.getFecha() + ", vendedor=" + this.getVendedor() + ", detalles=" + this.getDetalles() + ", subtotal=" + this.getSubtotal() + ", iva=" + this.getIva() + ", total=" + this.getTotal() + ")";
    }

    public Venta() {
    }

    public Venta(final Integer id, final String numeroFactura, final LocalDateTime fecha, final Empleado vendedor, final List<DetalleVenta> detalles, final BigDecimal subtotal, final BigDecimal iva, final BigDecimal total) {
        this.id = id;
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
        this.vendedor = vendedor;
        this.detalles = detalles;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
    }
}
