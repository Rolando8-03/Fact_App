package ni.edu.uam.fact_app.model;

import java.math.BigDecimal;

public class Producto {
    private Integer id;
    private String codigo;
    private String nombre;
    private Categoria categoria;
    private BigDecimal precioVenta;
    private int existencia;
    private String rutaImagen;
    private boolean activo;

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }

    public Integer getId() {
        return this.id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public BigDecimal getPrecioVenta() {
        return this.precioVenta;
    }

    public int getExistencia() {
        return this.existencia;
    }

    public String getRutaImagen() {
        return this.rutaImagen;
    }

    public boolean isActivo() {
        return this.activo;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setCodigo(final String codigo) {
        this.codigo = codigo;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(final Categoria categoria) {
        this.categoria = categoria;
    }

    public void setPrecioVenta(final BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public void setExistencia(final int existencia) {
        this.existencia = existencia;
    }

    public void setRutaImagen(final String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public void setActivo(final boolean activo) {
        this.activo = activo;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof Producto)) return false;
        final Producto other = (Producto) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        if (this.getExistencia() != other.getExistencia()) return false;
        if (this.isActivo() != other.isActivo()) return false;
        final java.lang.Object this$id = this.getId();
        final java.lang.Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final java.lang.Object this$codigo = this.getCodigo();
        final java.lang.Object other$codigo = other.getCodigo();
        if (this$codigo == null ? other$codigo != null : !this$codigo.equals(other$codigo)) return false;
        final java.lang.Object this$nombre = this.getNombre();
        final java.lang.Object other$nombre = other.getNombre();
        if (this$nombre == null ? other$nombre != null : !this$nombre.equals(other$nombre)) return false;
        final java.lang.Object this$categoria = this.getCategoria();
        final java.lang.Object other$categoria = other.getCategoria();
        if (this$categoria == null ? other$categoria != null : !this$categoria.equals(other$categoria)) return false;
        final java.lang.Object this$precioVenta = this.getPrecioVenta();
        final java.lang.Object other$precioVenta = other.getPrecioVenta();
        if (this$precioVenta == null ? other$precioVenta != null : !this$precioVenta.equals(other$precioVenta)) return false;
        final java.lang.Object this$rutaImagen = this.getRutaImagen();
        final java.lang.Object other$rutaImagen = other.getRutaImagen();
        if (this$rutaImagen == null ? other$rutaImagen != null : !this$rutaImagen.equals(other$rutaImagen)) return false;
        return true;
    }

    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof Producto;
    }

    @java.lang.Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getExistencia();
        result = result * PRIME + (this.isActivo() ? 79 : 97);
        final java.lang.Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final java.lang.Object $codigo = this.getCodigo();
        result = result * PRIME + ($codigo == null ? 43 : $codigo.hashCode());
        final java.lang.Object $nombre = this.getNombre();
        result = result * PRIME + ($nombre == null ? 43 : $nombre.hashCode());
        final java.lang.Object $categoria = this.getCategoria();
        result = result * PRIME + ($categoria == null ? 43 : $categoria.hashCode());
        final java.lang.Object $precioVenta = this.getPrecioVenta();
        result = result * PRIME + ($precioVenta == null ? 43 : $precioVenta.hashCode());
        final java.lang.Object $rutaImagen = this.getRutaImagen();
        result = result * PRIME + ($rutaImagen == null ? 43 : $rutaImagen.hashCode());
        return result;
    }

    public Producto() {
    }

    public Producto(final Integer id, final String codigo, final String nombre, final Categoria categoria, final BigDecimal precioVenta, final int existencia, final String rutaImagen, final boolean activo) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precioVenta = precioVenta;
        this.existencia = existencia;
        this.rutaImagen = rutaImagen;
        this.activo = activo;
    }
}
