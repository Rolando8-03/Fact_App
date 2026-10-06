package ni.edu.uam.fact_app.model;

import java.time.LocalDate;

public class Empleado {
    private Integer id;
    private String nombres;
    private String apellidos;
    private Cargo cargo;
    private LocalDate fechaContratacion;
    private boolean activo;

    public Integer getId() {
        return this.id;
    }

    public String getNombres() {
        return this.nombres;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public Cargo getCargo() {
        return this.cargo;
    }

    public LocalDate getFechaContratacion() {
        return this.fechaContratacion;
    }

    public boolean isActivo() {
        return this.activo;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setNombres(final String nombres) {
        this.nombres = nombres;
    }

    public void setApellidos(final String apellidos) {
        this.apellidos = apellidos;
    }

    public void setCargo(final Cargo cargo) {
        this.cargo = cargo;
    }

    public void setFechaContratacion(final LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public void setActivo(final boolean activo) {
        this.activo = activo;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof Empleado)) return false;
        final Empleado other = (Empleado) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        if (this.isActivo() != other.isActivo()) return false;
        final java.lang.Object this$id = this.getId();
        final java.lang.Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final java.lang.Object this$nombres = this.getNombres();
        final java.lang.Object other$nombres = other.getNombres();
        if (this$nombres == null ? other$nombres != null : !this$nombres.equals(other$nombres)) return false;
        final java.lang.Object this$apellidos = this.getApellidos();
        final java.lang.Object other$apellidos = other.getApellidos();
        if (this$apellidos == null ? other$apellidos != null : !this$apellidos.equals(other$apellidos)) return false;
        final java.lang.Object this$cargo = this.getCargo();
        final java.lang.Object other$cargo = other.getCargo();
        if (this$cargo == null ? other$cargo != null : !this$cargo.equals(other$cargo)) return false;
        final java.lang.Object this$fechaContratacion = this.getFechaContratacion();
        final java.lang.Object other$fechaContratacion = other.getFechaContratacion();
        if (this$fechaContratacion == null ? other$fechaContratacion != null : !this$fechaContratacion.equals(other$fechaContratacion)) return false;
        return true;
    }

    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof Empleado;
    }

    @java.lang.Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isActivo() ? 79 : 97);
        final java.lang.Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final java.lang.Object $nombres = this.getNombres();
        result = result * PRIME + ($nombres == null ? 43 : $nombres.hashCode());
        final java.lang.Object $apellidos = this.getApellidos();
        result = result * PRIME + ($apellidos == null ? 43 : $apellidos.hashCode());
        final java.lang.Object $cargo = this.getCargo();
        result = result * PRIME + ($cargo == null ? 43 : $cargo.hashCode());
        final java.lang.Object $fechaContratacion = this.getFechaContratacion();
        result = result * PRIME + ($fechaContratacion == null ? 43 : $fechaContratacion.hashCode());
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Empleado(id=" + this.getId() + ", nombres=" + this.getNombres() + ", apellidos=" + this.getApellidos() + ", cargo=" + this.getCargo() + ", fechaContratacion=" + this.getFechaContratacion() + ", activo=" + this.isActivo() + ")";
    }

    public Empleado() {
    }

    public Empleado(final Integer id, final String nombres, final String apellidos, final Cargo cargo, final LocalDate fechaContratacion, final boolean activo) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cargo = cargo;
        this.fechaContratacion = fechaContratacion;
        this.activo = activo;
    }
}
